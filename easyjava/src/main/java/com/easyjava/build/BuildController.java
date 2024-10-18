package com.easyjava.build;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildController {
    private static final Logger logger = LoggerFactory.getLogger(BuildController.class);

    public static void excute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_CONTROLLER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String serviceName = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE;
        String serviceBeanName = StringUtils.lowerCaseFirstLetter(serviceName);
        String serveImplName = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE + "Impl";
        String ctrlName = tableInfo.getBeanName() + "Controller";
        File svFile = new File(folder, ctrlName + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;


        try {
            out = new FileOutputStream(svFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_CONTROLLER + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import " + Constants.PACKAGE_VO + ".ResponseVO;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + serviceName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestBody;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestMapping;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RestController;");
            bw.newLine();
            bw.newLine();
            bw.write("import javax.annotation.Resource;");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();

            BuildComment.createClassComment(bw,ctrlName);
            bw.newLine();
            bw.write("\t@RestController");
            bw.newLine();
            bw.write("\t@RequestMapping(\"" + StringUtils.lowerCaseFirstLetter(tableInfo.getBeanName()) + "\")");
            bw.newLine();
            bw.write("\tpublic class " + ctrlName + " extends ABaseController{");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Resource");
            bw.newLine();
            bw.write("\tprivate " + serviceName + " " + serviceBeanName + ";");
            bw.newLine();

            BuildComment.createFieldComment(bw,"查询列表");
            bw.newLine();
            bw.write("\t@RequestMapping(\"loadDataList\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO loadDataList(" + tableInfo.getBeanParamName() + " query) {");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(" + serviceBeanName+ ".findListByPage(query));");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw,"新增");
            bw.newLine();
            bw.write("\t@RequestMapping(\"add\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO add(" + tableInfo.getBeanName() + " bean){");
            bw.newLine();
            bw.write("\t\tthis." + serviceBeanName + ".add(bean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO( null );");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();


            BuildComment.createFieldComment(bw,"批量新增");
            bw.newLine();
            bw.write("\t@RequestMapping(\"addBatch\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO addBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean){");
            bw.newLine();
            bw.write("\t\tthis." + serviceBeanName + ".addBatch(listBean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO( null );");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw,"批量新增或修改");
            bw.newLine();
            bw.write("\t@RequestMapping(\"addBatchOrUpdate\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO addBatchOrUpdate(@RequestBody List<" + tableInfo.getBeanName() + "> listBean){");
            bw.newLine();
            bw.write("\t\tthis." + serviceBeanName + ".addBatchOrUpdateBatch(listBean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO( null );");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
            for(Map.Entry<String, List<FieldInfo>> entry:tableInfo.getKeyIndexMap().entrySet()) {
                List<FieldInfo> keyFieldList = entry.getValue();
                Integer index2 = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder paramName = new StringBuilder();
                for (FieldInfo fieldInfo : keyFieldList) {
                    index2++;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    paramName.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    if (index2 < keyFieldList.size()) {
                        methodName.append("And");
                        paramName.append(" and ");
                    }
                    BuildComment.createFieldComment(bw,"根据" + methodName + "查询");
                    bw.newLine();
                    bw.write("\t@RequestMapping(\"get" + tableInfo.getBeanName() + "By" + methodName + "\")");
                    bw.newLine();
                    bw.write("\tpublic ResponseVO get" + tableInfo.getBeanName() + "By" + methodName + "(" + paramName + "){");
                    bw.newLine();
                    bw.write("\t\treturn getSuccessResponseVO(this." + serviceBeanName + ".get" + tableInfo.getBeanName() + "By" + methodName + "(" + fieldInfo.getPropertyName() + "));");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();

                    BuildComment.createFieldComment(bw,"根据" + methodName + "更新");
                    bw.newLine();
                    bw.write("\t@RequestMapping(\"update" + tableInfo.getBeanName() + "By" + methodName + "\")");
                    bw.newLine();
                    bw.write("\tpublic ResponseVO update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName()  + " bean, " +paramName + "){");
                    bw.newLine();
                    bw.write("\t\tthis." + serviceBeanName + ".update" + tableInfo.getBeanName() + "By" + methodName + "(bean, " + fieldInfo.getPropertyName() + ");");
                    bw.newLine();
                    bw.write("\t\treturn getSuccessResponseVO( null );");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();

                    BuildComment.createFieldComment(bw,"根据" + methodName + "删除");
                    bw.newLine();
                    bw.write("\t@RequestMapping(\"delete" + tableInfo.getBeanName() + "By" + methodName + "\")");
                    bw.newLine();
                    bw.write("\tpublic ResponseVO delete" + tableInfo.getBeanName() + "By" + methodName + "(" +paramName + "){");
                    bw.newLine();
                    bw.write("\t\tthis." + serviceBeanName + ".delete" + tableInfo.getBeanName() + "By" + methodName + "(" + fieldInfo.getPropertyName() + ");");
                    bw.newLine();
                    bw.write("\t\treturn getSuccessResponseVO( null );");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();
                }
            }
            bw.write("}");
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            logger.error("创建ServiceImpl失败");
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outw != null) {
                    outw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
