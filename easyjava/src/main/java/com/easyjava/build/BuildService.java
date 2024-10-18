package com.easyjava.build;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.build.BuildComment;
import com.easyjava.utils.DateUtils;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildService {
    private static final Logger logger = LoggerFactory.getLogger(com.easyjava.build.BuildService.class);

    public static void excute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICE);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String serviceName = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE;
        File svFile = new File(folder, serviceName + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;


        try {
            out = new FileOutputStream(svFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_SERVICE + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import " + Constants.PACKAGE_VO + "."  + "PaginationResultVO;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();

            BuildComment.createClassComment(bw,tableInfo.getComment() + Constants.SUFFIX_SERVICE);
            bw.newLine();
            bw.write("public interface " + serviceName + "{");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw,"根据条件查询列表");
            bw.newLine();
            bw.write("\tList<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw,"根据条件查询数量");
            bw.newLine();
            bw.write("\tInteger findCountByParam(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw,"分页查询");
            bw.newLine();
            bw.write("\tPaginationResultVO<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw,"新增");
            bw.newLine();
            bw.write("\tInteger add(" + tableInfo.getBeanName() + " bean);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw,"批量新增");
            bw.newLine();
            bw.write("\tInteger addBatch(List<" + tableInfo.getBeanName() + "> listBean);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw,"批量新增或修改");
            bw.newLine();
            bw.write("\tInteger addBatchOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean);");
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
                    bw.write("\t" + tableInfo.getBeanName() +  " get" + tableInfo.getBeanName() + "By" + methodName + "(" + paramName + ");");
                    bw.newLine();
                    bw.newLine();

                    BuildComment.createFieldComment(bw,"根据" + methodName + "更新");
                    bw.newLine();
                    bw.write("\tInteger update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName()  + " bean, " +paramName + ");");
                    bw.newLine();
                    bw.newLine();

                    BuildComment.createFieldComment(bw,"根据" + methodName + "删除");
                    bw.newLine();
                    bw.write("\tInteger delete" + tableInfo.getBeanName() + "By" + methodName + "(" +paramName + ");");
                    bw.newLine();
                    bw.newLine();
                }
            }
            bw.write("}");
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            logger.error("创建Service失败");
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
