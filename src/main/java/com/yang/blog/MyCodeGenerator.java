package com.yang.blog;


import com.github.yhl452493373.generator.CodeGenerator;
import com.github.yhl452493373.generator.CodeGeneratorConfig;
import com.github.yhl452493373.generator.DataSourceGeneratorConfig;

import java.util.HashMap;
import java.util.Map;

public class MyCodeGenerator {
    public static void main(String[] args) {
        //TODO 请先修改yml配置.包括application.yml中启动端口,上下文路径,要激活的其他配置
        //TODO application-redis.yml中的redis相关配置
        //TODO single-datasource或者multiple-datasource中的相关位置配置
        //TODO 根据情况修改生成代码后的config中的其他配置
        //TODO 执行后请按照LOG提示修改启动文件配置
        singleDataSource();
    }

    private static void singleDataSource() {
        DataSourceGeneratorConfig dsgc = new DataSourceGeneratorConfig();
        dsgc.setFileOverride(false);
        dsgc.setCacheEnabled(true);
        CodeGenerator.dataSourceCodeGenerate(dsgc);
        CodeGeneratorConfig cgc = new CodeGeneratorConfig(
                "blog",
                new String[]{
                        "article_tag", "article_file"
                },
                MyCodeGenerator.class.getPackage().getName()
        );
        cgc.setFileOverride(false);
        cgc.setEnableCache(true);
        cgc.setEnableRedis(false);
        CodeGenerator.baseCodeGenerate(cgc);
    }

    private static void multipleDataSource() {
        DataSourceGeneratorConfig dsgc = new DataSourceGeneratorConfig();
        dsgc.setMultiple(true);
        dsgc.setFileOverride(true);
        dsgc.setCacheEnabled(true);
        CodeGenerator.dataSourceCodeGenerate(dsgc);
        Map<String, String[]> dataSourceMap = new HashMap<>();
        dataSourceMap.put("psm", new String[]{"employee", "hidden_danger"});
        dataSourceMap.put("ime511", new String[]{"sys_user", "sys_areas"});
        dataSourceMap.keySet().forEach((key) -> {
            CodeGeneratorConfig cgc = new CodeGeneratorConfig(
                    key,
                    dataSourceMap.get(key),
                    MyCodeGenerator.class.getPackage().getName());
            cgc.setFileOverride(true);
            cgc.setEnableCache(true);
            cgc.setEnableRedis(true);
            cgc.setPackageController(cgc.getPackageController() + "." + key);
            cgc.setPackageEntity(cgc.getPackageEntity() + "." + key);
            cgc.setPackageMapper(cgc.getPackageMapper() + "." + key);
            cgc.setPackageService(cgc.getPackageService() + "." + key);
            cgc.setMapperPackage(cgc.getMapperPackage() + "." + key);
            CodeGenerator.baseCodeGenerate(cgc);
        });
    }
}
