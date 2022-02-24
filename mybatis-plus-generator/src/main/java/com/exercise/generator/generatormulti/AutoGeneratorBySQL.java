package com.exercise.generator.generatormulti;

import com.exercise.generator.generatormulti.config.*;
import com.exercise.generator.generatormulti.config.builder.ConfigBuilder;
import com.exercise.generator.generatormulti.config.rules.NamingStrategy;
import com.exercise.generator.generatormulti.engine.AbstractTemplateEngine;
import com.exercise.generator.generatormulti.engine.FreemarkerTemplateEngine;
import com.exercise.generator.generatormulti.parser.SqlParser;
import com.exercise.generator.generatormulti.po.*;
import com.exercise.generator.generatormulti.segment.*;
import com.exercise.generator.generatormulti.util.GeneratorUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: ZL
 * @date: 2019-06-19 11:25
 */
@Data
public class AutoGeneratorBySQL {

    /**
     * 主要配置信息
     */
    private MainConfig mainConfig;
    /**
     * 模板路径配置信息
     */
    private TemplateConfig templateConfig;
    /**
     * 数据源配置
     */
    private DataSourceConfig dataSourceConfig;
    /**
     * 包 相关配置
     */
    private PackageConfig packageConfig;
    /**
     * 全局 相关配置
     */
    private GlobalConfig globalConfig;
    /**
     * 策略配置
     */
    private StrategyConfig strategyConfig;
    /**
     * 模板引擎
     */
    @Setter(AccessLevel.PROTECTED)
    private AbstractTemplateEngine templateEngine;

    public void execute() {
        System.out.println("==========================准备生成文件...==========================");
        if (null == templateEngine) {
            templateEngine = new FreemarkerTemplateEngine();
        }
        //模板引擎初始化执行文件输出
        templateEngine.init(this.pretreatmentConfigBuilder()).mkdirs().batchOutput();
        //templateEngine.init(this.pretreatmentConfigBuilder());
        System.out.println("==========================文件生成完成！！！==========================");
    }

    /**
     * 预处理配置
     */
    private ConfigBuilder pretreatmentConfigBuilder() {
        SqlParser jqsp = new SqlParser(mainConfig.getSql());
        List<SqlSegment> sqlSegments = jqsp.getSqlSegments();
        SqlInfo sqlInfo = new SqlInfo();
        List<QueryPart> queryPartList = null;
        List<TablePart> tablePartList = null;
        for (SqlSegment sqlSegment : sqlSegments) {
            if (sqlSegment instanceof QueryParamSqlSegment) {
                sqlInfo.setQuerySQL(sqlSegment.getBody());
                queryPartList = ((QueryParamSqlSegment) sqlSegment).getQueryPartList();
            }
            if (sqlSegment instanceof TableSqlSegment) {
                sqlInfo.setTableSQL(sqlSegment.getBody());
                tablePartList = ((TableSqlSegment) sqlSegment).getTablePartList();
            }
            if (sqlSegment instanceof WhereSqlSegment) {
                sqlInfo.setWhereSQL(sqlSegment.getBody());
            }
            if (sqlSegment instanceof GroupBySqlSegment) {
                sqlInfo.setGroupBySQL(sqlSegment.getBody());
            }
            if (sqlSegment instanceof OrderBySqlSegment) {
                sqlInfo.setOrderBySQL(sqlSegment.getBody());
            }
        }
        if (tablePartList != null && tablePartList.size() > 0) {
            String[] includes = new String[tablePartList.size()];
            for (int i = 0; i < tablePartList.size(); i++) {
                includes[i] = tablePartList.get(i).getTableName();
            }
            ConfigBuilder configBuilder = this.getConfigBuilder(includes);
            List<TableInfo> tableInfoList = configBuilder.getTableInfoList();
            List<TableInfo> rtTableInfoList = new ArrayList<>();
            TableInfo tableInfo = new TableInfo();
//            tableInfo.setImportPackages(Serializable.class.getCanonicalName());
            tableInfo.setName(mainConfig.getBaseName());
            tableInfo.setEntityName(mainConfig.getBaseName());
            tableInfo.setComment(mainConfig.getDescription());
            tableInfo.setPageFlag(mainConfig.isPageFlag());
            tableInfo.setRequestMethod(mainConfig.getRequestMethod());
            if (mainConfig.getRequestUrl() == null || mainConfig.getRequestUrl().equals("")) {
                tableInfo.setRequestUrl("get" + mainConfig.getBaseName() + "ResVoList");
            } else {
                tableInfo.setRequestUrl(mainConfig.getRequestUrl());
            }
            tableInfo.setVersion(mainConfig.getVersion());
            rtTableInfoList.add(tableInfo);
            List<TableField> fields = new ArrayList<>();
            for (QueryPart item : queryPartList) {
                String tableName = this.getTableName(tablePartList, item.getTableAlias());
                fields.add(this.getTableField(tableInfoList, item, tableName));
            }
            tableInfo.setFields(fields);
            this.processTable(rtTableInfoList);
            configBuilder.setTableInfoList(rtTableInfoList);
            configBuilder.setSqlInfo(sqlInfo);
            return configBuilder;
        }
        return null;
    }

    private ConfigBuilder getConfigBuilder(String[] includes) {
        strategyConfig.setInclude(includes);
        ConfigBuilder config = new ConfigBuilder(packageConfig, dataSourceConfig, strategyConfig, templateConfig, globalConfig, mainConfig);
        return config;
    }

    private String getTableName(List<TablePart> tablePartList, String tableAlias) {
        for (TablePart item : tablePartList) {
            if (item.getTableAlias().equals(tableAlias)) {
                return item.getTableName();
            }
        }
        return null;
    }

    private TableField getTableField(List<TableInfo> tableInfoList, QueryPart queryPart, String tableName) {
        for (TableInfo ti : tableInfoList) {
            if (ti.getName().equals(tableName)) {
                for (TableField tf : ti.getFields()) {
                    if (tf.getName().equals(queryPart.getFieldName())) {
                        tf.setTableAlias(queryPart.getTableAlias());
                        tf.setQueryFlag(this.getQueryFlag(queryPart.getFieldAlias()));
                        tf.setNameAlias(queryPart.getFieldAlias());
                        tf.setPropertyName(this.processName(queryPart.getFieldAlias(), strategyConfig.getNaming(), strategyConfig.getFieldPrefix()));
                        try {
                            return (TableField) tf.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean getQueryFlag(String fieldAlias) {
        if (mainConfig.getParams() != null && mainConfig.getParams().length > 0) {
            for (String item : mainConfig.getParams()) {
                if (fieldAlias.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 处理表/字段名称
     *
     * @param name     ignore
     * @param strategy ignore
     * @param prefix   ignore
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy, String[] prefix) {
        boolean removePrefix = false;
        if (prefix != null && prefix.length != 0) {
            removePrefix = true;
        }
        String propertyName;
        if (removePrefix) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        return propertyName;
    }

    /**
     * 处理表对应的类名称
     *
     * @param tableList 表名称
     * @return 补充完整信息后的表
     */
    private List<TableInfo> processTable(List<TableInfo> tableList) {
        String[] tablePrefix = strategyConfig.getTablePrefix();
        for (TableInfo tableInfo : tableList) {
            String entityName;
            INameConvert nameConvert = strategyConfig.getNameConvert();
            if (null != nameConvert) {
                // 自定义处理实体名称
                entityName = nameConvert.entityNameConvert(tableInfo);
            } else {
                entityName = NamingStrategy.capitalFirst(processName(tableInfo.getName(), strategyConfig.getNaming(), tablePrefix));
            }
            if (GeneratorUtils.isNotEmpty(globalConfig.getEntityName())) {
                tableInfo.setConvert(true);
                tableInfo.setEntityName(String.format(globalConfig.getEntityName(), entityName));
            } else {
                tableInfo.setEntityName(strategyConfig, entityName);
            }
            if (GeneratorUtils.isNotEmpty(globalConfig.getMapperName())) {
                tableInfo.setMapperName(String.format(globalConfig.getMapperName(), entityName));
            } else {
                tableInfo.setMapperName(entityName + ConstVal.MAPPER);
            }
            if (GeneratorUtils.isNotEmpty(globalConfig.getXmlName())) {
                tableInfo.setXmlName(String.format(globalConfig.getXmlName(), entityName));
            } else {
                tableInfo.setXmlName(entityName + ConstVal.MAPPER);
            }
            if (GeneratorUtils.isNotEmpty(globalConfig.getServiceName())) {
                tableInfo.setServiceName(String.format(globalConfig.getServiceName(), entityName));
            } else {
                tableInfo.setServiceName("I" + entityName + ConstVal.SERVICE);
            }
            if (GeneratorUtils.isNotEmpty(globalConfig.getServiceImplName())) {
                tableInfo.setServiceImplName(String.format(globalConfig.getServiceImplName(), entityName));
            } else {
                tableInfo.setServiceImplName(entityName + ConstVal.SERVICE_IMPL);
            }
            if (GeneratorUtils.isNotEmpty(globalConfig.getControllerName())) {
                tableInfo.setControllerName(String.format(globalConfig.getControllerName(), entityName));
            } else {
                tableInfo.setControllerName(entityName + ConstVal.CONTROLLER);
            }
            if (GeneratorUtils.isNotEmpty(globalConfig.getControllerReqName())) {
                tableInfo.setControllerReqName(String.format(globalConfig.getControllerReqName(), entityName + "Req"));
            } else {
                tableInfo.setControllerReqName("Get" + entityName + "ReqVo");
            }
            if (GeneratorUtils.isNotEmpty(globalConfig.getControllerResName())) {
                tableInfo.setControllerResName(String.format(globalConfig.getControllerResName(), entityName + "Res"));
            } else {
                tableInfo.setControllerResName("Get" + entityName + "ResVo");
            }
        }
        return tableList;
    }
}
