package com.easyjava;

import com.easyjava.bean.TableInfo;
import com.easyjava.build.*;

import java.util.List;

public class RunApplication {
    public static void main(String[] args){

        List<TableInfo> tableInfoList = BuildTable.getTables();

        BuildBase.excute();

        for(TableInfo tableInfo:tableInfoList){

            BuildPo.excute(tableInfo);

            BuildQuery.excute(tableInfo);

            BuildMapper.excute(tableInfo);

            BuildMapperXml.excute(tableInfo);

            BuildService.excute(tableInfo);

            BuildServiceImpl.excute(tableInfo);

            BuildController.excute(tableInfo);
        }
    }
}
