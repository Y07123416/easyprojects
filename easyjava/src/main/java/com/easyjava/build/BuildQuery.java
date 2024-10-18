package com.easyjava.build;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildQuery {
    private static final Logger logger = LoggerFactory.getLogger(BuildQuery.class);
    public static void excute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_QUERY);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY;
        File queryFile = new File(folder,className + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;


        try{
            out = new FileOutputStream(queryFile);
            outw = new OutputStreamWriter(out,"utf8");
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_QUERY + ";");
            bw.newLine();
            bw.newLine();

            if(tableInfo.getHaveBigDecimal()){
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            bw.write("import java.util.Date;");
            bw.newLine();

            //构建类注释
            List<FieldInfo> fieldInfoListList = tableInfo.getFieldInfoList();
            List<FieldInfo> extendList = new ArrayList();
            BuildComment.createClassComment(bw,tableInfo.getComment() + "查询对象");
            bw.newLine();
            bw.write("public class " + className + " extends BaseQuery {");
            bw.newLine();

            for(FieldInfo fieldInfo:fieldInfoListList){
                extendList.add(fieldInfo);
                BuildComment.createFieldComment(bw,fieldInfo.getComment());
                bw.newLine();

                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();

                if(ArrayUtils.contains(Constants.SQL_STRING_TYPES,fieldInfo.getSqlType())){
                    String properName = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY;
                    bw.write("\tprivate " + fieldInfo.getJavaType() + " " + properName + ";");
                    bw.newLine();
                    bw.newLine();

                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setPropertyName(properName);
                    fuzzyField.setFieldName(fieldInfo.getFieldName());
                    fuzzyField.setJavaType(fieldInfo.getJavaType());
                    extendList.add(fuzzyField);
                }

                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType())){
                    String properNameS = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START;
                    String properNameE = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END;
                    bw.write("\tprivate String " +  properNameS + ";");
                    bw.newLine();
                    bw.write("\tprivate String " +  properNameE + ";");
                    bw.newLine();
                    bw.newLine();

                    FieldInfo startField = new FieldInfo();
                    startField.setPropertyName(properNameS);
                    startField.setFieldName(fieldInfo.getFieldName());
                    startField.setJavaType("String");
                    extendList.add(startField);

                    FieldInfo endField = new FieldInfo();
                    endField.setPropertyName(properNameE);
                    endField.setFieldName(fieldInfo.getFieldName());
                    endField.setJavaType("String");
                    extendList.add(endField);

                }
            }

            for(FieldInfo fieldInfo: extendList){
                String tempField = StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName());
                bw.write("\tpublic void set" + tempField + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {");
                bw.newLine();
                bw.write("\t\tthis." + fieldInfo.getPropertyName() + "= " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + tempField + "() {");
                bw.newLine();
                bw.write("\t\treturn " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
              /*  if(ArrayUtils.contains(Constants.SQL_STRING_TYPES,fieldInfo.getSqlType())){
                    String tempFuzzy = tempField + Constants.SUFFIX_BEAN_QUERY_FUZZY;
                    String propFuzzy = fieldInfo.getPropertyName()+ Constants.SUFFIX_BEAN_QUERY_FUZZY;
                    bw.write("\tpublic void set" + tempFuzzy + "(" + fieldInfo.getJavaType() + " " + propFuzzy + ") {");
                    bw.newLine();
                    bw.write("\t\tthis. " + propFuzzy + " = " + propFuzzy + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + tempFuzzy + "() {");
                    bw.newLine();
                    bw.write("\t\treturn " + propFuzzy + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                }
                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType())|| ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType())){
                    String tempStart = tempField + Constants.SUFFIX_BEAN_QUERY_TIME_START;
                    String tempEnd = tempField + Constants.SUFFIX_BEAN_QUERY_TIME_END;
                    String propStart = fieldInfo.getPropertyName()+ Constants.SUFFIX_BEAN_QUERY_TIME_START;
                    String propEnd = fieldInfo.getPropertyName()+ Constants.SUFFIX_BEAN_QUERY_TIME_END;
                    bw.write("\tpublic void set" + tempStart + "(String " + propStart + ") {");
                    bw.newLine();
                    bw.write("\t\tthis. " + propStart + " = " + propStart + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.write("\tpublic String " + "get" + tempStart + "() {");
                    bw.newLine();
                    bw.write("\t\treturn " + propStart + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.write("\tpublic void set" + tempEnd + "(String " + propEnd + ") {");
                    bw.newLine();
                    bw.write("\t\tthis. " + propEnd + " = " + propEnd + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.write("\tpublic String " + "get" + tempEnd + "() {");
                    bw.newLine();
                    bw.write("\t\treturn " + propEnd + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                }

               */
            }
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建Query失败");
        } finally {
            try{
                if(bw != null){
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                if(outw != null){
                    outw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

