package com.easyjava.build;

import com.easyjava.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildBase {

    private static final Logger logger = LoggerFactory.getLogger(BuildBase.class);
    public static void excute(){
        List<String> headInfoList = new ArrayList();
        //生成日期枚举
        headInfoList.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(headInfoList,"DateTimePatternEnum", Constants.PATH_ENUMS);
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_UTILS + ";");
        build(headInfoList,"DateUtils", Constants.PATH_UTILS);
        //生成BaseMapper
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_MAPPERS + ";");
        build(headInfoList,"BaseMapper",Constants.PATH_MAPPERS);
        //生成SimplePage
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_QUERY + ";");
        headInfoList.add("import " + Constants.PACKAGE_ENUMS + ".PageSize;");
        build(headInfoList,"SimplePage",Constants.PATH_QUERY);
        //生成pageSize枚举
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(headInfoList,"PageSize",Constants.PATH_ENUMS);
        //生成BaseQuery
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_QUERY + ";");
        build(headInfoList,"BaseQuery",Constants.PATH_QUERY);
        //生成PaginationResultVO
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_VO + ";");
        build(headInfoList,"PaginationResultVO",Constants.PATH_VO);
        //生成BussinessException
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_EXCEPTION + ";");
        headInfoList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;");
        build(headInfoList,"BussinessException",Constants.PATH_EXCEPTION);
        //生成ResponseCodeEnum枚举
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(headInfoList,"ResponseCodeEnum",Constants.PATH_ENUMS);
        //生成BaseController
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_CONTROLLER + ";");
        headInfoList.add("import " + Constants.PACKAGE_VO + ".ResponseVO;");
        headInfoList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;");
        build(headInfoList,"ABaseController",Constants.PATH_CONTROLLER);
        //生成ResponseVO
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_VO + ";");
        build(headInfoList,"ResponseVO",Constants.PATH_VO);
        //生成GlobalExceptionHandlerController
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_CONTROLLER + ";");
        headInfoList.add("import " + Constants.PACKAGE_VO + ".ResponseVO;");
        headInfoList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;");
        headInfoList.add("import " + Constants.PACKAGE_EXCEPTION + ".BussinessException;");
        headInfoList.add("import org.slf4j.Logger;");
        headInfoList.add("import org.slf4j.LoggerFactory;");
        headInfoList.add("import org.springframework.dao.DuplicateKeyException;");
        headInfoList.add("import org.springframework.web.bind.annotation.ExceptionHandler;");
        headInfoList.add("import org.springframework.web.bind.annotation.RestControllerAdvice;");
        headInfoList.add("import org.springframework.web.servlet.NoHandlerFoundException;");
        headInfoList.add("import javax.servlet.http.HttpServletRequest;");
        headInfoList.add("import java.net.BindException;");
        build(headInfoList,"AGlobalExceptionHandlerController",Constants.PATH_CONTROLLER);
    }

    private static void build(List<String> headInfoList, String fileName, String outputPath) {
        File folder = new File(outputPath);
        if(!folder.exists()){
            folder.mkdirs();
        }

        File javaFile = new File(outputPath,fileName + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        InputStream in = null;
        InputStreamReader inr = null;
        BufferedReader br = null;
        try{
            out = new FileOutputStream(javaFile);
            outw = new OutputStreamWriter(out,"utf8");
            bw = new BufferedWriter(outw);

            String templatePath = BuildBase.class.getClassLoader().getResource("template/" + fileName + ".txt").getPath();
            in = new FileInputStream(templatePath);
            inr = new InputStreamReader(in,"utf8");
            br = new BufferedReader(inr);

            int index = 0;
            for(String headInfo: headInfoList){
                index ++;
                bw.write(headInfo);
                bw.newLine();
                if(headInfo.contains("package") || index == headInfoList.size()){
                    bw.newLine();
                }
            }

            String lineInfo = null;
            while((lineInfo= br.readLine()) != null) {
                bw.write(lineInfo);
                bw.newLine();
            }
            bw.flush();

        } catch (Exception e) {
            logger.error("生成基础类: {},失败:", fileName, e);
        } finally {
            try{
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                if(inr != null){
                    inr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
