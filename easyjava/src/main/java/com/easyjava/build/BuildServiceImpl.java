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

public class BuildServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(BuildController.class);

    public static void excute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICE_IMPL);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String serviceName = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE;
        String serviceBeanName = StringUtils.lowerCaseFirstLetter(serviceName);
        String serveImplName = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE + "Impl";
        String mapperName = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
        String mapperBeanName = StringUtils.lowerCaseFirstLetter(mapperName);
        File svFile = new File(folder, serveImplName + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;


        try {
            out = new FileOutputStream(svFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_SERVICE_IMPL + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import " + Constants.PACKAGE_QUERY + ".SimplePage" + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_ENUMS + ".PageSize;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + serviceName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + "."  + "PaginationResultVO;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_MAPPERS + "." + mapperName + ";");
            bw.newLine();
            bw.write("import org.springframework.stereotype.Service;");
            bw.newLine();
            bw.newLine();
            bw.write("import javax.annotation.Resource;");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();

            BuildComment.createClassComment(bw,serveImplName);
            bw.newLine();
            bw.write("\t@Service(\"" + serviceBeanName + "\")");
            bw.newLine();
            bw.write("\tpublic class " + serveImplName + " implements " + serviceName + "{");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Resource");
            bw.newLine();
            bw.write("\tprivate " + mapperName + "<" + tableInfo.getBeanName() + "," + tableInfo.getBeanParamName() + "> " + mapperBeanName + ";");
            bw.newLine();
            BuildComment.createFieldComment(bw,"根据条件查询列表");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic List<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() + " query){");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".selectList(query);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw,"根据条件查询数量");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer findCountByParam(" + tableInfo.getBeanParamName() + " query){");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".selectCount(query);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw,"分页查询");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic PaginationResultVO<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() + " query){");
            bw.newLine();
            bw.write("\t\tInteger count = this.findCountByParam(query);");
            bw.newLine();
            bw.write("\t\tInteger pageSize = query.getPageSize() == null? PageSize.SIZE15.getSize():query.getPageSize();");
            bw.newLine();
            bw.write("\t\tSimplePage page = new SimplePage(query.getPageNo(),count,pageSize);");
            bw.newLine();
            bw.write("\t\tquery.setSimplePage(page);");
            bw.newLine();
            bw.write("\t\tList<" + tableInfo.getBeanName() + "> list = this.findListByParam(query);");
            bw.newLine();
            bw.write("\t\treturn new PaginationResultVO(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw,"新增");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer add(" + tableInfo.getBeanName() + " bean){");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".insert(bean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();


            BuildComment.createFieldComment(bw,"批量新增");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer addBatch(List<" + tableInfo.getBeanName() + "> listBean){");
            bw.newLine();
            bw.write("\t\tif(listBean == null || listBean.isEmpty()) {");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("}");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".insertBatch(listBean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw,"批量新增或修改");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer addBatchOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean){");
            bw.newLine();
            bw.write("\t\tif(listBean == null || listBean.isEmpty()) {");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\t}");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".insertOrUpdateBatch(listBean);");
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
                    bw.write("\t@Override");
                    bw.newLine();
                    bw.write("\tpublic " + tableInfo.getBeanName() +  " get" + tableInfo.getBeanName() + "By" + methodName + "(" + paramName + "){");
                    bw.newLine();
                    bw.write("\t\treturn this." + mapperBeanName + ".selectBy" + methodName + "(" + fieldInfo.getPropertyName() + ");");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();

                    BuildComment.createFieldComment(bw,"根据" + methodName + "更新");
                    bw.newLine();
                    bw.write("\t@Override");
                    bw.newLine();
                    bw.write("\tpublic Integer update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName()  + " bean, " +paramName + "){");
                    bw.newLine();
                    bw.write("\t\treturn this." + mapperBeanName + ".updateBy" + methodName + "(bean, " +  fieldInfo.getPropertyName() + ");");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();

                    BuildComment.createFieldComment(bw,"根据" + methodName + "删除");
                    bw.newLine();
                    bw.write("\t@Override");
                    bw.newLine();
                    bw.write("\tpublic Integer delete" + tableInfo.getBeanName() + "By" + methodName + "(" +paramName + "){");
                    bw.newLine();
                    bw.write("\t\treturn this." + mapperBeanName + ".deleteBy" + methodName + "(" + fieldInfo.getPropertyName() + ");");
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
