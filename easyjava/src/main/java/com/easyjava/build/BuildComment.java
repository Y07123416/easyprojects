package com.easyjava.build;

import com.easyjava.bean.Constants;
import com.easyjava.utils.DateUtils;

import java.io.BufferedWriter;
import java.util.Date;

public class BuildComment {
    public static void createClassComment(BufferedWriter bw,String classComment) throws Exception{
        bw.write("/**");
        bw.newLine();
        bw.write(" * @author:" + Constants.AUTHOR);
        bw.newLine();
        bw.write(" * @description:" + classComment);
        bw.newLine();
        bw.write(" * @date:" + DateUtils.format(new Date(), DateUtils._YYYYMMDD));
        bw.newLine();
        bw.write("*/");
    }

    public static void createFieldComment(BufferedWriter bw,String fieldComment) throws Exception{
        bw.write("\t/**");
        bw.newLine();
        bw.write(fieldComment==null?"":"\t * " + fieldComment);
        bw.newLine();
        bw.write("\t*/");
    }

    public static void createMethodComment(){

    }
}
