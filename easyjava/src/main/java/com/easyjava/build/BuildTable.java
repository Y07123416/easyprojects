package com.easyjava.build;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.JsonUtils;
import com.easyjava.utils.PropertiesUtils;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTable {

    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static Connection conn = null;
    private static String SQL_SHOW_TABLE_STATUS = "show table status";

    private static String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";

    private static String SQL_SHOW_TABLE_INDEX = "show index from %s";

    static {
        String driverName = PropertiesUtils.getString("db.driver.name");
        String url = PropertiesUtils.getString("db.url");
        String userName = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");
        try{
            Class.forName(driverName);
            conn = DriverManager.getConnection(url,userName,password);
        } catch (Exception e) {
            logger.error("数据库连接失败",e);
        }
    }
    //读表
    public static List<TableInfo> getTables(){
        PreparedStatement ps = null;
        ResultSet tableResult = null;

        List<TableInfo> tableInfoList = new ArrayList();
        try{
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = ps.executeQuery();
            //读所有表
            while(tableResult.next()){
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");
                //logger.info("tableName:{},comment:{}",tableName,comment);

                String beanName = tableName;
                if(Constants.IGNORE_TABLE_PREFIX){
                    beanName = tableName.substring(beanName.indexOf("_") + 1);
                }
                beanName = processField(beanName,true);

                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName+Constants.SUFFIX_BEAN_QUERY);

                readFieldInfo(tableInfo);
                getKeyIndexInfo(tableInfo);
            //    logger.info("tableInfo:{}",JsonUtils.convertObj2Json(tableInfo));
                //每一次循环结束将tableInfo注入tableInfoList
                tableInfoList.add(tableInfo);
            }
            logger.info("表:{}",JsonUtils.convertObj2Json(tableInfoList));
        } catch (Exception e) {
            logger.error("读取表失败",e);
        }finally{
            if(tableResult != null){
                try{
                    tableResult.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try{
                    ps.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return tableInfoList;
    }
    //读属性
    public static void readFieldInfo(TableInfo tableInfo){
        PreparedStatement ps = null;
        ResultSet fieldResult = null;

        List<FieldInfo> fieldInfoList = new ArrayList();
        List<FieldInfo> extendFieldList = new ArrayList();
        try{
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS,tableInfo.getTableName()));
            fieldResult = ps.executeQuery();

            Boolean haveDateTime = false;
            Boolean haveDate = false;
            Boolean haveBigDecimal = false;

            while(fieldResult.next()) {
                String field = fieldResult.getString("field");
                String type = fieldResult.getString("type");
                String extra = fieldResult.getString("extra");
                String comment = fieldResult.getString("comment");
                //logger.info("field:{},type:{},extra:{},comment:{}", field, type, extra, comment);

                if(type.indexOf("(") > 0){
                    type = type.substring(0,type.indexOf("("));
                }
                String propertyName = processField(field,false);

                FieldInfo fieldInfo = new FieldInfo();
                fieldInfoList.add(fieldInfo);

                fieldInfo.setFieldName(field);
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setSqlType(type);
                fieldInfo.setJavaType(processJavaType(type));
                fieldInfo.setComment(comment);
                fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra) ? true : false);

                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,type)){
                    haveDateTime = true;
                }
                if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,type)){
                    haveDate = true;
                }
                if(ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES,type)){
                    haveBigDecimal = true;
                }
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPES,type)){
                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setJavaType(fieldInfo.getJavaType());
                    fuzzyField.setPropertyName(propertyName + Constants.SUFFIX_BEAN_QUERY_FUZZY);
                    fuzzyField.setFieldName(fieldInfo.getFieldName());
                    fuzzyField.setSqlType(type);
                    extendFieldList.add(fuzzyField);
                }

                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType())){
                    String properNameS = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START;
                    String properNameE = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END;

                    FieldInfo timeStartField = new FieldInfo();
                    timeStartField.setJavaType("String");
                    timeStartField.setPropertyName(properNameS);
                    timeStartField.setFieldName(fieldInfo.getFieldName());
                    timeStartField.setSqlType(type);
                    extendFieldList.add(timeStartField);

                    FieldInfo timeEndField = new FieldInfo();
                    timeEndField.setJavaType("String");
                    timeEndField.setPropertyName(properNameE);
                    timeEndField.setFieldName(fieldInfo.getFieldName());
                    timeEndField.setSqlType(type);
                    extendFieldList.add(timeEndField);
                }

                tableInfo.setHaveDateTime(haveDateTime);
                tableInfo.setHaveDate(haveDate);
                tableInfo.setHaveBigDecimal(haveBigDecimal);
            }
            tableInfo.setFieldInfoList(fieldInfoList);
            tableInfo.setExtendFieldList(extendFieldList);
        } catch (Exception e) {
            logger.error("读取表失败",e);
        }finally{
            if(fieldResult != null){
                try{
                    fieldResult.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try{
                    ps.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    //读索引
    public static List<FieldInfo> getKeyIndexInfo(TableInfo tableInfo){
        PreparedStatement ps = null;
        ResultSet fieldResult = null;

        List<FieldInfo> fieldInfoList = new ArrayList();
        try{
            Map<String,FieldInfo> tempMap = new HashMap();
            for(FieldInfo fieldInfo: tableInfo.getFieldInfoList()){
                tempMap.put(fieldInfo.getFieldName(),fieldInfo);
            }
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX,tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while(fieldResult.next()) {
                String keyName = fieldResult.getString("key_name");
                Integer nonUnique = fieldResult.getInt("non_unique");
                String columnName = fieldResult.getString("column_name");

                if (nonUnique == 1) {
                    continue;
                }
                List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
                if (null == keyFieldList) {
                    keyFieldList = new ArrayList();
                    tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
                }
                keyFieldList.add(tempMap.get(columnName));
            }

        } catch (Exception e) {
            logger.error("读取索引失败",e);
        }finally{
            if(fieldResult != null){
                try{
                    fieldResult.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try{
                    ps.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return fieldInfoList;
    }

    public static String processField(String field,Boolean upperCaseFirstLetter){

        StringBuffer sb = new StringBuffer();
        String[] fields = field.split("_");
        sb.append(upperCaseFirstLetter? StringUtils.upperCaseFirstLetter(fields[0]):fields[0]);
        for(int i =1, len = fields.length; i < len; i ++){
            sb.append(StringUtils.upperCaseFirstLetter(fields[i]));
        }
        return sb.toString();
    }

    private static String processJavaType(String type){
        if(ArrayUtils.contains(Constants.SQL_INTEGER_TYPES,type)){
            return "Integer";
        } else if(ArrayUtils.contains(Constants.SQL_LONG_TYPES,type)){
            return "Long";
        } else if(ArrayUtils.contains(Constants.SQL_STRING_TYPES,type)){
            return "String";
        } else if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,type) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,type)){
            return "Date";
        } else if(ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES,type)){
            return "BigDecimal";
        } else{
            throw new RuntimeException("无法识别的类型" + type);
        }
    }
}
