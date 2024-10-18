package com.easyjava.build;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.DateUtils;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BuildPo {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void excute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_PO);
        if(!folder.exists()){
            folder.mkdirs();
        }
        File poFile = new File(folder,tableInfo.getBeanName() + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;


        try{
            out = new FileOutputStream(poFile);
            outw = new OutputStreamWriter(out,"utf8");
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_PO + ";");
            bw.newLine();
            bw.newLine();


            if(tableInfo.getHaveDateTime() || tableInfo.getHaveDate()){
                bw.write("import " + Constants.PACKAGE_ENUMS + ".DateTimePatternEnum;");
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_UTILS +".DateUtils;");
                bw.newLine();
            }

            if(tableInfo.getHaveDateTime() || tableInfo.getHaveDate()){
                bw.write(Constants.BEAN_DATE_FORMAT_CLASS + ";");
                bw.newLine();
                bw.write(Constants.BEAN_DATE_UNFORMAT_CLASS + ";");
                bw.newLine();
            }

            Boolean haveIgnoreBean = false;
            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                if(ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","),fieldInfo.getFieldName())){
                    haveIgnoreBean = true;
                    break;
                }
            }
            if(haveIgnoreBean){
                bw.write(Constants.IGNORE_BEAN_TOJSON_CLASS + ";");
                bw.newLine();
            }

            bw.newLine();
            bw.write("import java.io.Serializable;");
            bw.newLine();

            if(tableInfo.getHaveBigDecimal()){
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            bw.write("import java.util.Date;");
            bw.newLine();

            //构建类注释
            BuildComment.createClassComment(bw,tableInfo.getComment());
            bw.newLine();
            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bw.newLine();

            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                BuildComment.createFieldComment(bw,fieldInfo.getComment());
                bw.newLine();

                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType())){
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_mm_ss));
                    bw.newLine();
                    bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_mm_ss));
                    bw.newLine();
                }
                if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType())){
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                    bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                }
                if(ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","),fieldInfo.getFieldName())){
                    bw.write("\t" + String.format(Constants.IGNORE_BEAN_TOJSON_EXPRESSION,DateUtils.YYYY_MM_DD));
                    bw.newLine();
                }
                    bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                    bw.newLine();
            }

            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                String tempField = StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName());
                bw.write("\tpublic void set" + tempField + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {");
                bw.newLine();
                bw.write("\t\tthis." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + tempField + "() {");
                bw.newLine();
                bw.write("\t\treturn " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
            }
            StringBuffer toString = new StringBuffer();
            //重写toString方法
            Integer index = 0;
            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                String propName = fieldInfo.getPropertyName();
                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType())){
                    propName = "DateUtils.format(" + fieldInfo.getPropertyName() + ", DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())";
                }else if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType())){
                    propName = "DateUtils.format(" + fieldInfo.getPropertyName() + ", DateTimePatternEnum.YYYY_MM_DD.getPattern())";
                }
                toString.append(fieldInfo.getComment() + ": \" + " + "(" + fieldInfo.getPropertyName() + " == null ? \"空\": " + propName + ")");
                if(index < tableInfo.getFieldInfoList().size() - 1) {
                    toString.append(" + ").append("\",");
                }
                index ++;
            }
            String toStringStr = toString.toString();
            toStringStr = "\"" + toStringStr;
           // toStringStr.substring(0,toStringStr.lastIndexOf("+"));
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic String toString() {");
            bw.newLine();
            bw.write("\t\treturn " + toStringStr + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建Po失败");
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
