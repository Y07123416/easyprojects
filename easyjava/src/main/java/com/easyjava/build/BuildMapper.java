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

public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapper.class);
    public static void excute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_MAPPERS);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String mapperName = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
        File mpFile = new File(folder,mapperName + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;


        try{
            out = new FileOutputStream(mpFile);
            outw = new OutputStreamWriter(out,"utf8");
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_MAPPERS + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import org.apache.ibatis.annotations.Param;");
            bw.newLine();
            bw.newLine();

            //构建类注释
            BuildComment.createClassComment(bw,tableInfo.getComment() + "Mapper");
            bw.newLine();
            bw.write("public interface " + mapperName + "<T, P> extends BaseMapper {");
            bw.newLine();
            bw.newLine();

            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            StringBuilder methodName = new StringBuilder();
            StringBuilder methodParam = new StringBuilder();
            for(Map.Entry<String,List<FieldInfo>> entry: keyIndexMap.entrySet()){
                List<FieldInfo> keyFieldInfoList = entry.getValue();

                Integer index = 0;
                for(FieldInfo fieldInfo: keyFieldInfoList){
                    index ++;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if(index < keyFieldInfoList.size()){
                        methodName.append("And");
                    }

                    methodParam.append("@Param(\"" + fieldInfo.getPropertyName() +  "\") " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    if(index < keyFieldInfoList.size()){
                        methodParam.append(", ");
                    }
                }
                BuildComment.createFieldComment(bw,"根据" + methodName.toString() + "查询");
                bw.newLine();
                bw.write("\tT selectBy" + methodName + "(" + methodParam + ");");
                bw.newLine();

                BuildComment.createFieldComment(bw,"根据" + methodName.toString() + "更新");
                bw.newLine();
                bw.write("\tInteger updateBy" + methodName + "(@Param(\"bean\") T t, " + methodParam + ");");
                bw.newLine();

                BuildComment.createFieldComment(bw,"根据" + methodName + "删除");
                bw.newLine();
                bw.write("\tInteger deleteBy" + methodName + "(" + methodParam + ");");
                bw.newLine();
            }
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建Mapper失败");
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
