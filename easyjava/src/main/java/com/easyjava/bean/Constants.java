package com.easyjava.bean;

import com.easyjava.utils.PropertiesUtils;

public class Constants {
    public static Boolean IGNORE_TABLE_PREFIX;
    public static String SUFFIX_BEAN_QUERY;
    public static String SUFFIX_BEAN_QUERY_FUZZY;
    public static String SUFFIX_BEAN_QUERY_TIME_START;
    public static String SUFFIX_BEAN_QUERY_TIME_END;
    public static String SUFFIX_MAPPERS;
    public static String SUFFIX_SERVICE;
    //需要忽略的属性
    public static String IGNORE_BEAN_TOJSON_FIELD;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;
    //日期序列化、反序列化
    public static String BEAN_DATE_FORMAT_EXPRESSION;
    public static String BEAN_DATE_FORMAT_CLASS;
    public static String BEAN_DATE_UNFORMAT_EXPRESSION;
    public static String BEAN_DATE_UNFORMAT_CLASS;
    public static String PATH_BASE;
    public static String PACKAGE_BASE;
    public static String PATH_MAPPERS;
    public static String PACKAGE_MAPPERS;
    public static String PATH_SERVICE;
    public static String PACKAGE_SERVICE;
    public static String PATH_SERVICE_IMPL;
    public static String PACKAGE_SERVICE_IMPL;
    public static String PATH_CONTROLLER;
    public static String PACKAGE_CONTROLLER;

    private static String PATH_JAVA = "java";
    private static String PATH_RESOURCE = "resources";

    public static String PATH_MAPPERS_XMLS;
    public static String PACKAGE_PO;
    public static String PATH_PO;
    public static String PACKAGE_QUERY;
    public static String PATH_QUERY;
    public static String PATH_UTILS;
    public static String PACKAGE_UTILS;
    public static String PATH_VO;
    public static String PACKAGE_VO;
    public static String PATH_ENUMS;
    public static String PACKAGE_ENUMS;
    public static String PATH_EXCEPTION;
    public static String PACKAGE_EXCEPTION;
    public static String AUTHOR;

    static{
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));
        SUFFIX_BEAN_QUERY = PropertiesUtils.getString("suffix.bean.query");
        SUFFIX_BEAN_QUERY_FUZZY = PropertiesUtils.getString("suffix.bean.query.fuzzy");
        SUFFIX_BEAN_QUERY_TIME_START = PropertiesUtils.getString("suffix.bean.query.time.start");
        SUFFIX_BEAN_QUERY_TIME_END = PropertiesUtils.getString("suffix.bean.query.time.end");
        SUFFIX_MAPPERS = PropertiesUtils.getString("suffix.mappers");
        SUFFIX_SERVICE = PropertiesUtils.getString("suffix.service");

        IGNORE_BEAN_TOJSON_FIELD = PropertiesUtils.getString("ignore.bean.tojson.field");
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getString("ignore.bean.tojson.express");
        IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getString("ignore.bean.tojson.class");

        BEAN_DATE_FORMAT_EXPRESSION = PropertiesUtils.getString("bean.date.format.expression");
        BEAN_DATE_FORMAT_CLASS = PropertiesUtils.getString("bean.date.class");
        BEAN_DATE_UNFORMAT_EXPRESSION = PropertiesUtils.getString("bean.date.unformat.expression");
        BEAN_DATE_UNFORMAT_CLASS = PropertiesUtils.getString("bean.date.unclass");

        PATH_BASE = PropertiesUtils.getString("path.base");
        PATH_BASE = PATH_BASE + PATH_JAVA + "/" + PropertiesUtils.getString("package.base");
        PATH_BASE = PATH_BASE.replace(".","/");
        PATH_PO = PATH_BASE + "/" + PropertiesUtils.getString("package.po");
        PATH_PO = PATH_PO.replace(".","/");
        PATH_QUERY = PATH_BASE + "/" + PropertiesUtils.getString("package.query");
        PATH_QUERY = PATH_QUERY.replace(".","/");
        PATH_UTILS = PATH_BASE + "/" + PropertiesUtils.getString("package.utils");
        PATH_UTILS = PATH_UTILS.replace(".","/");
        PATH_VO = PATH_BASE + "/" + PropertiesUtils.getString("package.vo");
        PATH_VO = PATH_VO.replace(".","/");
        PATH_ENUMS = PATH_BASE + "/" + PropertiesUtils.getString("package.enums");
        PATH_ENUMS = PATH_ENUMS.replace(".","/");
        PATH_EXCEPTION = PATH_BASE + "/" + PropertiesUtils.getString("package.exception");
        PATH_EXCEPTION = PATH_EXCEPTION.replace(".","/");
        PATH_MAPPERS = PATH_BASE + "/" + PropertiesUtils.getString("package.mappers");
        PATH_MAPPERS = PATH_MAPPERS.replace(".","/");
        PATH_MAPPERS_XMLS = PropertiesUtils.getString("path.base") + PATH_RESOURCE + "/" + PropertiesUtils.getString("package.mappers").replace(".","/");
        PATH_SERVICE = PATH_BASE + "/" + PropertiesUtils.getString("package.service");
        PATH_SERVICE = PATH_SERVICE.replace(".","/");
        PATH_SERVICE_IMPL = PATH_BASE + "/" + PropertiesUtils.getString("package.service.impl");
        PATH_SERVICE_IMPL = PATH_SERVICE_IMPL.replace(".","/");
        PATH_CONTROLLER = PATH_BASE + "/" + PropertiesUtils.getString("package.controller");
        PATH_CONTROLLER = PATH_CONTROLLER.replace(".","/");


        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");
        PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getString("package.query");
        PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils");
        PACKAGE_VO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.vo");
        PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums");
        PACKAGE_EXCEPTION = PACKAGE_BASE + "." + PropertiesUtils.getString("package.exception");
        PACKAGE_MAPPERS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mappers");
        PACKAGE_SERVICE = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service");
        PACKAGE_SERVICE_IMPL = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service.impl");
        PACKAGE_CONTROLLER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.controller");
        AUTHOR = PropertiesUtils.getString("author.name");
    }

    public final static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime","timestamp"};
    public final static String[] SQL_DATE_TYPES = new String[]{"date"};
    public final static String[] SQL_DECIMAL_TYPES = new String[]{"decimal","double","float"};
    public final static String[] SQL_STRING_TYPES = new String[]{"char","varchar","mediumtext","longtext"};
    public final static String[] SQL_INTEGER_TYPES = new String[]{"int","tinyint"};
    public final static String[] SQL_LONG_TYPES = new String[]{"bigint"};

    public static void main(String[] args){

        System.out.println(PATH_BASE);
        System.out.println(PATH_PO);
        System.out.println(PATH_QUERY);
        System.out.println(PACKAGE_BASE);
        System.out.println(PACKAGE_PO);
        System.out.println(PACKAGE_QUERY);
    }
}
