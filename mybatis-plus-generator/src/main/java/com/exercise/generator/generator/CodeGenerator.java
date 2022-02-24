package com.exercise.generator.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZL
 * @description 单表代码生成
 * @date 2019/10/14
 */
public class CodeGenerator {
    private static String PROJECT_PATH = System.getProperty("user.dir") + "/generator/code";
    private static String BASE_PACK = "com.sanywind.admin";
    private static String ENTITY_SUFFIX = "Entity";
    private static String ENTITY_FOLDER = "entity";
    private static String REQUEST_LIST_SUFFIX = "ListRequestVo";
    private static String REQUEST_CREATE_SUFFIX = "CreateRequestVo";
    private static String REQUEST_UPDATE_SUFFIX = "UpdateRequestVo";
    private static String RESPONSE_LIST_SUFFIX = "ListResponseVo";
    private static String RESPONSE_GET_SUFFIX = "GetResponseVo";
    /*驱动连接的URL*/
    private static String DB_URL = "jdbc:mysql://10.255.10.146:3306/sany-ren-ant-platform?autoReconnect=true&createDatabaseIfNotExist=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    /*驱动名称*/
    private static String DB_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    /*数据库连接用户名*/
    private static String DB_USER_NAME = "root";
    /*数据库连接密码*/
    private static String DB_PASSWORD = "root";

    /**
     * 表前缀
     */
    private static String TABLE_PREFIX = "";
    private static String VO_EXCLUDE_PREFIX = "Sys";
    /**
     * 表名
     */
    private static String[] TABLE_NAMES = {"sys_user", "sys_dept"};

    public static void main(String[] args) {
        String basePackPath = BASE_PACK.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        mpg.setGlobalConfig(initGlobalConfig());
        // 数据源配置
        mpg.setDataSource(initDataSourceConfig());
        // 包名配置
        mpg.setPackageInfo(initPackageConfig());
        // 模板配置
        mpg.setTemplate(initTemplateConfig());
        // 数据库表配置
        mpg.setStrategy(initStrategyConfig());
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entitySuffix", ENTITY_SUFFIX);
                map.put("entityFolder", ENTITY_FOLDER);
                map.put("requestListSuffix", REQUEST_LIST_SUFFIX);
                map.put("responseListSuffix", RESPONSE_LIST_SUFFIX);
                map.put("responseGetSuffix", RESPONSE_GET_SUFFIX);
                map.put("requestCreateSuffix", REQUEST_CREATE_SUFFIX);
                map.put("requestUpdateSuffix", REQUEST_UPDATE_SUFFIX);
                this.setMap(map);
            }
        };

        // 自定义输出配置,自定义配置会被优先输出
        List<FileOutConfig> focList = new ArrayList<>();
       // xml
        focList.add(new FileOutConfig("/template/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return PROJECT_PATH + "/src/main/resources/" + "/mapper/" + tableInfo.getEntityName().replace("Entity", "") + "Mapper" + StringPool.DOT_XML;
            }
        });
//        // GetReqVo
//        focList.add(new FileOutConfig("/template/GetReqVo.java.ftl") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                String entityPath = tableInfo.getEntityName().replace("Entity", "");
//                return PROJECT_PATH + "/src/main/java/" + basePackPath + "/pojo/vo/" + entityPath.toLowerCase() + "/Get" + entityPath + "ReqVo" + StringPool.DOT_JAVA;
////                return PROJECT_PATH + "/src/main/java/" + basePackPath + "/model/vo/Get" + entityPath + "ReqVo" + StringPool.DOT_JAVA;
//            }
//        });
//        // GetResVo
        focList.add(new FileOutConfig("/template/ResVo.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String entityPath = tableInfo.getEntityName().replace("Entity", "");
                String packageName = entityPath.replace(VO_EXCLUDE_PREFIX, "");
                return PROJECT_PATH + "/src/main/java/" + basePackPath + "/pojo/vo/" + packageName.toLowerCase() + "/" + entityPath + "VO" + StringPool.DOT_JAVA;
            }
        });
        // AddReqVo
//        focList.add(new FileOutConfig("/template/AddReqVo.java.ftl") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                String entityPath = tableInfo.getEntityName().replace("Entity", "");
////                return PROJECT_PATH + "/src/main/java/" + basePackPath + "/model/vo/" + entityPath.toLowerCase() + "/Add" + entityPath + "ReqVo" + StringPool.DOT_JAVA;
//            }
//        });
        // ModifyReqVo
//        focList.add(new FileOutCojj hnfig("/template/ModifyReqVo.java.ftl") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                String entityPath = tableInfo.getEntityName().replace("Entity", "");
//                return PROJECT_PATH + "/src/main/java/" + basePackPath + "/model/vo/" + entityPath.toLowerCase() + "/Modify" + entityPath + "ReqVo" + StringPool.DOT_JAVA;
//            }
//        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.execute();
    }

    private static StrategyConfig initStrategyConfig() {
        StrategyConfig strategyConfig = new StrategyConfig();
        //表前缀
        strategyConfig.setTablePrefix(TABLE_PREFIX);
        //自定义基础的Entity类，公共字段
        //strategyConfig.setSuperEntityColumns(null);
        //需要包含的表名，允许正则表达式（与exclude二选一配置）
        strategyConfig.setInclude(TABLE_NAMES);
        //需要排除的表名，允许正则表达式
        //strategyConfig.setExclude(null);
        //字段前缀
        //strategyConfig.setFieldPrefix(null);
        //自定义继承的Entity类全称，带包名
        strategyConfig.setSuperEntityClass("com.sanywind.common.base.BaseEntity");
        //是否大写命名
        strategyConfig.setCapitalMode(false);
        //是否跳过视图
        strategyConfig.setSkipView(false);
        //数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
        strategyConfig.setNameConvert(null);
        //数据库表映射到实体的命名策略
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        //【实体】是否生成字段常量（默认 false）
        strategyConfig.setEntityColumnConstant(false);
        //【实体】是否为构建者模型（默认 false）
        strategyConfig.setEntityBuilderModel(false);
        //【实体】是否为lombok模型（默认 false）
        strategyConfig.setEntityLombokModel(true);
        //Boolean类型字段是否移除is前缀（默认 false）
        strategyConfig.setEntityBooleanColumnRemoveIsPrefix(true);



        //自定义继承的Mapper类全称，带包名
        strategyConfig.setSuperMapperClass(null);
        //自定义继承的Service类全称，带包名
        strategyConfig.setSuperServiceClass(null);
        //自定义继承的ServiceImpl类全称，带包名
        strategyConfig.setSuperServiceImplClass(null);
        //自定义继承的Controller类全称，带包名
        strategyConfig.setSuperControllerClass(null);
        strategyConfig.setEntitySerialVersionUID(false);
        //生成 @RestController 控制器
        strategyConfig.setRestControllerStyle(false);
        //驼峰转连字符
        strategyConfig.setControllerMappingHyphenStyle(true);
        //是否生成实体时，生成字段注解
        strategyConfig.setEntityTableFieldAnnotationEnable(true);
        //乐观锁属性名称
        strategyConfig.setVersionFieldName(null);
        //逻辑删除属性名称
        strategyConfig.setLogicDeleteFieldName("is_delete");
        //表填充字段
        List<TableFill> tableFills = new ArrayList<TableFill>();
        TableFill isDelete = new TableFill("is_delete", FieldFill.INSERT);
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill createMan = new TableFill("create_man", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time", FieldFill.UPDATE);
        TableFill updateMan = new TableFill("update_man", FieldFill.UPDATE);
        tableFills.add(isDelete);
        tableFills.add(createTime);
        tableFills.add(createMan);
        tableFills.add(updateTime);
        tableFills.add(updateMan);
        strategyConfig.setTableFillList(tableFills);
        return strategyConfig;
    }

    private static GlobalConfig initGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        //生成文件的输出目录
        globalConfig.setOutputDir(PROJECT_PATH + "/src/main/java");
        //是否覆盖已有文件
        //默认值：false
        globalConfig.setFileOverride(true);
        //是否打开输出目录
        //默认值：true
        globalConfig.setOpen(true);
        //是否在xml中添加二级缓存配置
        //默认值：false
        globalConfig.setEnableCache(false);
        //开发人员
        globalConfig.setAuthor("ranqing");
        //开启 Kotlin 模式
        //默认值：false
        globalConfig.setKotlin(false);
        //开启 swagger2 模式
        //默认值：false
        globalConfig.setSwagger2(true);
        //开启 ActiveRecord 模式
        //默认值：false
        globalConfig.setActiveRecord(false);
        //开启 BaseResultMap
        //默认值：false
        globalConfig.setBaseResultMap(true);
        //时间类型对应策略
        //默认值：TIME_PACK
        globalConfig.setDateType(DateType.TIME_PACK);
        //开启 baseColumnList
        //默认值：false
        globalConfig.setBaseColumnList(true);
        //实体命名方式
        //默认值：null 例如：%sEntity 生成 UserEntity
//        globalConfig.setEntityName("%s" + ENTITY_SUFFIX);
        globalConfig.setEntityName("%s");
        //mapper 命名方式
        //默认值：null 例如：%sDao 生成 UserDao
        globalConfig.setMapperName(null);
        //Mapper xml 命名方式
        //默认值：null 例如：%sDao 生成 UserDao.xml
        globalConfig.setXmlName(null);
        //service 命名方式
        //默认值：null 例如：%sBusiness 生成 UserBusiness
        globalConfig.setServiceName("%sService");
        //service impl 命名方式
        //默认值：null 例如：%sBusinessImpl 生成 UserBusinessImpl
        globalConfig.setServiceImplName("%sServiceImpl");
        //controller 命名方式
        //默认值：null 例如：%sAction 生成 UserAction
        globalConfig.setControllerName("%sController");
        //指定生成的主键的ID类型
        //默认值：null
        globalConfig.setIdType(null);
        return globalConfig;
    }

    private static DataSourceConfig initDataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        //数据库信息查询类
        //默认由 dbType 类型决定选择对应数据库内置实现
        dataSourceConfig.setDbQuery(new MySqlQuery());
        //数据库类型
        //该类内置了常用的数据库类型【必须】
        dataSourceConfig.setDbType(DbType.MYSQL);

        //数据库 schema name
        //例如 PostgreSQL 可指定为 public
        dataSourceConfig.setSchemaName("public");
        //类型转换
        //默认由 dbType 类型决定选择对应数据库内置实现
//        dataSourceConfig.setTypeConvert(new MySqlTypeConvert());

        dataSourceConfig.setTypeConvert(new MySqlTypeConvert(){
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String t = fieldType.toLowerCase();
                if (t.contains("char")) {
                    return DbColumnType.STRING;
                } else if (t.contains("bigint")) {
                    return DbColumnType.LONG;
                } else if (t.contains("tinyint(1)")) {
                    return DbColumnType.INTEGER;
                } else if (t.contains("int")) {
                    return DbColumnType.INTEGER;
                } else if (t.contains("text")) {
                    return DbColumnType.STRING;
                } else if (t.contains("bit")) {
                    return DbColumnType.INTEGER;
                } else if (t.contains("decimal")) {
                    return DbColumnType.BIG_DECIMAL;
                } else if (t.contains("clob")) {
                    return DbColumnType.CLOB;
                } else if (t.contains("blob")) {
                    return DbColumnType.BLOB;
                } else if (t.contains("binary")) {
                    return DbColumnType.BYTE_ARRAY;
                } else if (t.contains("float")) {
                    return DbColumnType.FLOAT;
                } else if (t.contains("double")) {
                    return DbColumnType.DOUBLE;
                } else if (t.contains("json") || t.contains("enum")) {
                    return DbColumnType.STRING;
                } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
                    switch (globalConfig.getDateType()) {
                        case ONLY_DATE:
                            return DbColumnType.DATE;
                        case SQL_PACK:
                            switch (t) {
                                case "date":
                                    return DbColumnType.DATE_SQL;
                                case "time":
                                    return DbColumnType.TIME;
                                case "year":
                                    return DbColumnType.DATE_SQL;
                                default:
                                    return DbColumnType.TIMESTAMP;
                            }
                        case TIME_PACK:
                            switch (t) {
                                case "date":
                                    return DbColumnType.LOCAL_DATE;
                                case "time":
                                    return DbColumnType.LOCAL_TIME;
                                case "year":
                                    return DbColumnType.YEAR;
                                default:
                                    return DbColumnType.LOCAL_DATE_TIME;
                            }
                    }
                }
                return DbColumnType.STRING;
            }
        });
        //驱动连接的URL
        dataSourceConfig.setUrl(DB_URL);
        //驱动名称
        dataSourceConfig.setDriverName(DB_DRIVER_NAME);
        //数据库连接用户名
        dataSourceConfig.setUsername(DB_USER_NAME);
        //数据库连接密码
        dataSourceConfig.setPassword(DB_PASSWORD);
        return dataSourceConfig;
    }

    private static PackageConfig initPackageConfig() {
        PackageConfig packageConfig = new PackageConfig();
        //父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
        packageConfig.setParent(BASE_PACK);
        //父包模块名
        packageConfig.setModuleName(null);
        //Entity包名
        packageConfig.setEntity("pojo." + ENTITY_FOLDER);
        //Service包名
        packageConfig.setService("service");
        //Service Impl包名
        packageConfig.setServiceImpl("service.impl");
        //Mapper包名
        packageConfig.setMapper("mapper");
        //Mapper XML包名
        packageConfig.setXml(null);
//        packageConfig.setXml("pojo." + ENTITY_FOLDER);
        //Controller包名
        packageConfig.setController("controller");
        //路径配置信息
        packageConfig.setPathInfo(null);
        return packageConfig;
    }

    private static TemplateConfig initTemplateConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        //Java 实体类模板
        templateConfig.setEntity("/template/entity.java");
        //Kotin 实体类模板
        //templateConfig.setEntityKt("");
        //Service 类模板
//        templateConfig.setService("/template/service.java");
        templateConfig.setService(null);
        //Service impl 实现类模板
//        templateConfig.setServiceImpl("/template/serviceImpl.java");
        templateConfig.setServiceImpl(null);
        //mapper 模板
        templateConfig.setMapper("/template/mapper.java");
        //mapper xml 模板
//        templateConfig.setXml("/templateBySQL/mapper.xml");
        templateConfig.setXml(null);
        //controller 控制器模板
//        templateConfig.setController("/template/controller.java");
        templateConfig.setController(null);
        return templateConfig;
    }
}
