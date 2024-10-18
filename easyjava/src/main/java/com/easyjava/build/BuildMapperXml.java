package com.easyjava.build;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildMapperXml {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapperXml.class);
    private static final String BASE_COLUMN_LIST = "base_column_list";
    private static final String BASE_QUERY_CONDITION = "base_query_condition";
    private static final String QUERY_CONDITION = "query_condition";
    private static final String BASE_QUERY_CONDITION_EXTEND = "base_query_condition_extend";

    public static void excute(TableInfo tableInfo) {
        logger.info(Constants.PATH_MAPPERS_XMLS);
        File folder = new File(Constants.PATH_MAPPERS_XMLS);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
        File mpFile = new File(folder, className + ".xml");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;


        try {
            out = new FileOutputStream(mpFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);

            bw.write("<?xml version=\"1.0\" encoding=\"utf8\"?>");
            bw.newLine();
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            bw.newLine();
            bw.write("\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bw.newLine();
            bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPERS + "." + className + "\">");
            bw.newLine();

            bw.write("\t<!--实体映射-->");
            bw.newLine();
            String poClass = Constants.PACKAGE_PO + "." + tableInfo.getBeanName();
            bw.write("\t<resultMap id=\"base_result_map\" type=\"" + poClass + "\">");
            bw.newLine();
            //找到唯一主键自增id？
            FieldInfo idField = null;
            Map<String,List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for(Map.Entry<String,List<FieldInfo>> entry:keyIndexMap.entrySet()){
                if("PRIMARY".equals(entry.getKey())){
                    List<FieldInfo> fieldInfoList = entry.getValue();
                    if(fieldInfoList.size() == 1) {
                        idField = fieldInfoList.get(0);
                        break;
                    }
                }
            }
            for(FieldInfo fieldInfo:tableInfo.getFieldInfoList()){
                bw.write("\t\t<!--" + fieldInfo.getComment() + "-->");
                bw.newLine();
                String key = "";
                if(idField != null && fieldInfo.getPropertyName().equals(idField.getPropertyName())){
                    key = "id";
                } else {
                    key = "result";
                }
                bw.write("\t\t<" + key + " column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() +"\"/>");
                bw.newLine();
            }
            bw.write("\t</resultMap>");
            bw.newLine();

            //通用查询列
            bw.newLine();
            bw.write("\t<!--通用查询列-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">");
            bw.newLine();
            StringBuilder columnBuilder = new StringBuilder();
            for(FieldInfo fieldInfo:tableInfo.getFieldInfoList()){
                columnBuilder.append(fieldInfo.getFieldName()).append(",");
            }
            String columnBuilderStr = columnBuilder.substring(0,columnBuilder.lastIndexOf(","));
            bw.write("\t\t" + columnBuilderStr);
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();


            //基础查询条件
            bw.newLine();
            bw.write("\t<!--基础查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">");
            bw.newLine();
            for(FieldInfo fieldInfo:tableInfo.getFieldInfoList()){
                String strQuery = "";
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPES,fieldInfo.getSqlType())){
                    strQuery = " and query." + fieldInfo.getPropertyName() + " != ''";
                }

                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + strQuery + "\">");
                bw.newLine();
                bw.write("\t\t\tand id = #{query." + fieldInfo.getPropertyName() + "}");
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();
            }
            bw.write("\t</sql>");
            bw.newLine();

            //拓展查询列
            bw.newLine();
            bw.write("\t<!--拓展查询列-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION_EXTEND + "\">");
            bw.newLine();
            StringBuilder queryBuilder = new StringBuilder();
            for(FieldInfo fieldInfo:tableInfo.getExtendFieldList()){
                String andWhere = "";
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPES,fieldInfo.getSqlType())){
                    andWhere = "and " + fieldInfo.getFieldName() + " like concat('%', #{query." + fieldInfo.getPropertyName() + "}, '%')";
                }else if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType())){
                    if(fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_START)) {
                        andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " >= str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d') ]]>";
                    } else if(fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_END)){
                        andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " < str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d'), interval -1 day]]>";
                    }
                }
                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() + "!= ''\">");
                bw.newLine();
                bw.write("\t\t\t" + andWhere);
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();
            }
            bw.write("\t</sql>");
            bw.newLine();

            //拓展的查询条件
            bw.newLine();
            bw.write("\t<!--拓展查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + QUERY_CONDITION + "\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION_EXTEND + "\"/>");
            bw.newLine();
            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();

            //查询列表
            bw.newLine();
            bw.write("\t<!--查询列表-->");
            bw.newLine();
            bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
            bw.newLine();
            bw.write("\t\tSELECT");
            bw.newLine();
            bw.write("\t\t<include refid=\"" + BASE_COLUMN_LIST + "\"/>");
            bw.newLine();
            bw.write("\t\tFROM " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<include refid=\"" + QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.orderBy!=null\">order by ${query.orderBy}</if>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.simplePage!=null\">limit #{query.simplePage.start},#{query.simplePage.end}</if>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();

            //查询数量
            bw.newLine();
            bw.write("\t<!--查询数量-->");
            bw.newLine();
            bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">");
            bw.newLine();
            bw.write("\t\tSELECT count(1) FROM " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<include refid=\"" + QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();

            //单条插入
            bw.newLine();
            bw.write("\t<!--插入(匹配有值的字段)-->");
            bw.newLine();
            bw.write("\t<insert id=\"insert\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">");
            bw.newLine();

            FieldInfo autoIncrementField = null;
            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                if(fieldInfo.getAutoIncrement()!= null && fieldInfo.getAutoIncrement()){
                    autoIncrementField = fieldInfo;
                    break;
                }
            }
            if(autoIncrementField != null) {
                bw.write("\t\t<selectKey keyProperty=\"bean." + autoIncrementField.getFieldName() + "\" resultType=\"" + autoIncrementField.getJavaType() + "\"" +
                        " order=\"AFTER\">");
                bw.newLine();
                bw.write("\t\t\tSELECT LAST_INSERT_ID()");
                bw.newLine();
                bw.write("\t\t</selectKey>");
            }
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();

            //插入或更新
            bw.newLine();
            bw.write("\t<!--插入或更新-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdate\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">");
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t\t<!--主键不能被更改-->");
            bw.newLine();
            bw.write("\t\ton DUPLICATE key update");
            bw.newLine();

            //找到主键->插入或更新
            bw.newLine();
            Map<String,String> keyTempMap = new HashMap();
            for(Map.Entry<String,List<FieldInfo>> entry:keyIndexMap.entrySet()){
                List<FieldInfo> fieldInfoList = entry.getValue();
                for(FieldInfo fieldInfo: fieldInfoList){
                    keyTempMap.put(fieldInfo.getFieldName(), fieldInfo.getFieldName());
                }
            }
            bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
            bw.newLine();
            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()) {
                if(keyTempMap.get(fieldInfo.getFieldName()) != null){
                    continue;
                }
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + " = VALUES(" + fieldInfo.getFieldName() + "),");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();

            //批量插入
            bw.newLine();
            bw.write("\t<!--批量插入-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertBatch\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">");
            bw.newLine();
            StringBuffer insertFieldBuffer = new StringBuffer();
            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                if(fieldInfo.getAutoIncrement()){
                    continue;
                }
                insertFieldBuffer.append(fieldInfo.getFieldName()).append(",");
            }
            String insertFieldStr = insertFieldBuffer.substring(0,insertFieldBuffer.lastIndexOf(","));
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "(" + insertFieldStr + ")values");
            bw.newLine();
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            bw.newLine();
            StringBuilder propertyBuffer = new StringBuilder();
            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                if(fieldInfo.getAutoIncrement()){
                    continue;
                }
                propertyBuffer.append("#{item." + fieldInfo.getPropertyName() + "},");
            }
            String propertyStr = propertyBuffer.substring(0,propertyBuffer.lastIndexOf(","));
            bw.write("\t\t\t(" + propertyStr + ")");
            bw.newLine();
            bw.write("\t\t</foreach>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();

            //批量插入或更新
            bw.newLine();
            bw.write("\t<!--批量插入或更新-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">");
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "(" + insertFieldStr + ")values");
            bw.newLine();
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            bw.newLine();
          /*  for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                if(fieldInfo.getAutoIncrement()){
                    continue;
                }
                propertyBuffer.append("#{item." + fieldInfo.getPropertyName() + "},");
            }
           */
            bw.write("\t\t\t(" + propertyStr + ")");
            bw.newLine();
            bw.write("\t\t</foreach>");
            bw.newLine();

            bw.write("\t\ton DUPLICATE key update");
            bw.newLine();
            Integer index = 0;
            for(FieldInfo field:tableInfo.getFieldInfoList()){
                index ++;
                String dot = "";
                if(keyTempMap.get(field.getFieldName()) != null){
                    continue;
                }
                if(index < tableInfo.getFieldInfoList().size()) {
                    dot = ",";
                }
                bw.write("\t\t" + field.getFieldName() + " = VALUES(" + field.getFieldName() + ")" + dot);
                bw.newLine();
            }
            bw.write("\t</insert>");
            bw.newLine();

            //根据主键查询、更新和删除
            bw.newLine();
            for(Map.Entry<String,List<FieldInfo>> entry:tableInfo.getKeyIndexMap().entrySet()) {
                List<FieldInfo> keyFieldList = entry.getValue();
                Integer index2 = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder paramName = new StringBuilder();
                for(FieldInfo fieldInfo:keyFieldList){
                    index2 ++;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    paramName.append(fieldInfo.getFieldName() + "=#{" + fieldInfo.getPropertyName() + "}");
                    if (index2 < keyFieldList.size()) {
                        methodName.append("And");
                        paramName.append(" and ");
                    }
                    bw.write("\t<!--根据" + methodName + "查询-->");
                    bw.newLine();
                    bw.write("\t<select id=\"selectBy" + methodName + "\"" + " resultMap=\"base_result_map\">");
                    bw.newLine();
                    bw.write("\t\tselect <include refid=\"base_column_list\"/> from " + tableInfo.getTableName() + " where " + paramName);
                    bw.newLine();
                    bw.write("\t</select>");
                    bw.newLine();
                    bw.newLine();

                    bw.write("\t<!--根据" + methodName + "删除-->");
                    bw.newLine();
                    bw.write("\t<delete id=\"deleteBy" + methodName + "\">");
                    bw.newLine();
                    bw.write("\t\tdelete from " + tableInfo.getTableName() + " where " + paramName);
                    bw.newLine();
                    bw.write("\t</delete>");
                    bw.newLine();
                    bw.newLine();

                    bw.write("\t<!--根据" + methodName + "更新-->");
                    bw.newLine();
                    bw.write("\t<update id=\"updateBy" + methodName + "\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">");
                    bw.newLine();
                    bw.write("\t\tupdate " + tableInfo.getTableName());
                    bw.newLine();
                    bw.write("\t\t<set>");
                    bw.newLine();
                    Integer index3 = 0;
                    for(FieldInfo field: tableInfo.getFieldInfoList()){
                        String str = "";
                        index3 ++;
                        bw.write("\t\t\t<if test =\"bean." + field.getPropertyName() +" != null\">");
                        bw.newLine();
                        if(index3 < tableInfo.getFieldInfoList().size()){
                            str = ",";
                        }
                        bw.write("\t\t\t\t" + field.getFieldName() + "=#{bean." + field.getPropertyName() + "}" + str);
                        bw.newLine();
                        bw.write("\t\t\t</if>");
                        bw.newLine();
                    }
                    bw.write("\t\t</set>");
                    bw.newLine();
                    bw.write("\t\twhere " + paramName);
                    bw.newLine();
                    bw.write("\t</update>");
                    bw.newLine();
                    bw.newLine();
                }
            }
            bw.write("</mapper>");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建Mapper Xml失败");
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