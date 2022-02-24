
package com.exercise.generator.generatormulti.config.builder;

import com.exercise.generator.generatormulti.annotation.DbType;
import com.exercise.generator.generatormulti.config.*;
import com.exercise.generator.generatormulti.config.rules.NamingStrategy;
import com.exercise.generator.generatormulti.po.SqlInfo;
import com.exercise.generator.generatormulti.po.TableField;
import com.exercise.generator.generatormulti.po.TableInfo;
import com.exercise.generator.generatormulti.util.GeneratorUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class ConfigBuilder {

    /**
     * 主要配置信息
     */
    private final MainConfig mainConfig;
    /**
     * 模板路径配置信息
     */
    private final TemplateConfig template;
    /**
     * 数据库配置
     */
    private final DataSourceConfig dataSourceConfig;
    /**
     * SQL连接
     */
    private Connection connection;
    /**
     * SQL语句类型
     */
    private IDbQuery dbQuery;
    private String superEntityClass;
    private String superMapperClass;
    /**
     * service超类定义
     */
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;
    /**
     * 数据库表信息
     */
    private List<TableInfo> tableInfoList;
    /**
     * 包配置详情
     */
    private Map<String, String> packageInfo;
    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;
    /**
     * 策略配置
     */
    private StrategyConfig strategyConfig;
    /**
     * 全局配置信息
     */
    private GlobalConfig globalConfig;
//    /**
//     * 注入配置信息
//     */
//    private InjectionConfig injectionConfig;
    /**
     * 是否支持注释
     */
    private boolean commentSupported;
    /**
     * sql解析信息
     */
    private SqlInfo sqlInfo;

    /**
     * 在构造器中处理配置
     *
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     *                         //     * @param template         模板配置
     * @param globalConfig     全局配置
     */
    public ConfigBuilder(PackageConfig packageConfig,
                         DataSourceConfig dataSourceConfig,
                         StrategyConfig strategyConfig,
                         TemplateConfig template,
                         GlobalConfig globalConfig,
                         MainConfig mainConfig) {
        if (null == mainConfig) {
            this.mainConfig = new MainConfig();
        } else {
            this.mainConfig = mainConfig;
        }
        // 全局配置
        if (null == globalConfig) {
            this.globalConfig = new GlobalConfig();
        } else {
            this.globalConfig = globalConfig;
        }
        // 模板配置
        if (null == template) {
            this.template = new TemplateConfig();
        } else {
            this.template = template;
        }
        // 包配置
        if (null == packageConfig) {
            handlerPackage(this.template, this.globalConfig, new PackageConfig());
        } else {
            handlerPackage(this.template, this.globalConfig, packageConfig);
        }
        this.dataSourceConfig = dataSourceConfig;
        handlerDataSource(dataSourceConfig);
        // 策略配置
        if (null == strategyConfig) {
            this.strategyConfig = new StrategyConfig();
        } else {
            this.strategyConfig = strategyConfig;
        }
        //SQLITE 数据库不支持注释获取
        commentSupported = !dataSourceConfig.getDbType().equals(DbType.SQLITE);

        handlerStrategy(this.strategyConfig);
    }

    // ************************ 曝露方法 BEGIN*****************************

    /**
     * 所有包配置信息
     *
     * @return 包配置
     */
    public Map<String, String> getPackageInfo() {
        return packageInfo;
    }


    /**
     * 所有路径配置
     *
     * @return 路径配置
     */
    public Map<String, String> getPathInfo() {
        return pathInfo;
    }


    public String getSuperEntityClass() {
        return superEntityClass;
    }


    public String getSuperMapperClass() {
        return superMapperClass;
    }


    /**
     * 获取超类定义
     *
     * @return 完整超类名称
     */
    public String getSuperServiceClass() {
        return superServiceClass;
    }


    public String getSuperServiceImplClass() {
        return superServiceImplClass;
    }


    public String getSuperControllerClass() {
        return superControllerClass;
    }


    /**
     * 表信息
     *
     * @return 所有表信息
     */
    public List<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    public ConfigBuilder setTableInfoList(List<TableInfo> tableInfoList) {
        this.tableInfoList = tableInfoList;
        return this;
    }


    /**
     * 模板路径配置信息
     *
     * @return 所以模板路径配置信息
     */
    public TemplateConfig getTemplate() {
        return template == null ? new TemplateConfig() : template;
    }

    // ****************************** 曝露方法 END**********************************

    /**
     * 处理包配置
     *
     * @param template     TemplateConfig
     * @param globalConfig GlobalConfig
     * @param config       PackageConfig
     */
    private void handlerPackage(TemplateConfig template, GlobalConfig globalConfig, PackageConfig config) {
        // 包信息
        packageInfo = new HashMap<>(10);
        packageInfo.put(ConstVal.MODULE_NAME, config.getModuleName());
        packageInfo.put(ConstVal.ENTITY, joinPackage(config.getParent(), config.getEntity()));
        packageInfo.put(ConstVal.MAPPER, joinPackage(config.getParent(), config.getMapper()));
        packageInfo.put(ConstVal.XML, joinPackage("", config.getMapper()));
        packageInfo.put(ConstVal.SERVICE, joinPackage(config.getParent(), config.getService()));
        packageInfo.put(ConstVal.SERVICE_IMPL, joinPackage(config.getParent(), config.getServiceImpl()));
        packageInfo.put(ConstVal.CONTROLLER, joinPackage(config.getParent(), config.getController()));
        packageInfo.put(ConstVal.CONTROLLER_REQ, joinPackage(config.getParent(), config.getControllerReq()));
        packageInfo.put(ConstVal.CONTROLLER_RES, joinPackage(config.getParent(), config.getControllerRes()));
        if (mainConfig.isPageFlag()) {
            packageInfo.put(ConstVal.PAGE_REQUEST, joinPackage(config.getParent(), config.getPageRequest()));
            packageInfo.put(ConstVal.PAGE_RESPONSE, joinPackage(config.getParent(), config.getPageResponse()));
        }

        // 自定义路径
        Map<String, String> configPathInfo = config.getPathInfo();
        if (null != configPathInfo) {
            pathInfo = configPathInfo;
        } else {
            // 生成路径信息
            pathInfo = new HashMap<>(8);
            setPathInfo(pathInfo, template.getEntity(getGlobalConfig().isKotlin()), globalConfig.getOutputDir(), ConstVal.ENTITY_PATH, ConstVal.ENTITY);
            setPathInfo(pathInfo, template.getMapper(), globalConfig.getOutputDir(), ConstVal.MAPPER_PATH, ConstVal.MAPPER);
            setPathInfo(pathInfo, template.getXml(), globalConfig.getOutputXMLDir(), ConstVal.XML_PATH, ConstVal.XML);
            setPathInfo(pathInfo, template.getService(), globalConfig.getOutputDir(), ConstVal.SERVICE_PATH, ConstVal.SERVICE);
            setPathInfo(pathInfo, template.getServiceImpl(), globalConfig.getOutputDir(), ConstVal.SERVICE_IMPL_PATH, ConstVal.SERVICE_IMPL);
            setPathInfo(pathInfo, template.getController(), globalConfig.getOutputDir(), ConstVal.CONTROLLER_PATH, ConstVal.CONTROLLER);
            setPathInfo(pathInfo, template.getControllerReq(), globalConfig.getOutputDir(), ConstVal.CONTROLLER_REQ_PATH, ConstVal.CONTROLLER_REQ);
            setPathInfo(pathInfo, template.getControllerRes(), globalConfig.getOutputDir(), ConstVal.CONTROLLER_RES_PATH, ConstVal.CONTROLLER_RES);
            if (mainConfig.isPageFlag()) {
                setPathInfo(pathInfo, template.getPageRequest(), globalConfig.getOutputDir(), ConstVal.PAGE_REQUEST_PATH, ConstVal.PAGE_REQUEST);
                setPathInfo(pathInfo, template.getPageResponse(), globalConfig.getOutputDir(), ConstVal.PAGE_RESPONSE_PATH, ConstVal.PAGE_RESPONSE);
            }
        }
    }

    private void setPathInfo(Map<String, String> pathInfo, String template, String outputDir, String path, String module) {
        if (GeneratorUtils.isNotEmpty(template)) {
            pathInfo.put(path, joinPath(outputDir, packageInfo.get(module)));
        }
    }

    /**
     * 处理数据源配置
     *
     * @param config DataSourceConfig
     */
    private void handlerDataSource(DataSourceConfig config) {
        connection = config.getConn();
        dbQuery = config.getDbQuery();
    }


    /**
     * 处理数据库表 加载数据库表、列、注释相关数据集
     *
     * @param config StrategyConfig
     */
    private void handlerStrategy(StrategyConfig config) {
        processTypes(config);
        tableInfoList = getTablesInfo(config);
    }


    /**
     * 处理superClassName,IdClassType,IdStrategy配置
     *
     * @param config 策略配置
     */
    private void processTypes(StrategyConfig config) {
        if (GeneratorUtils.isEmpty(config.getSuperServiceClass())) {
            superServiceClass = ConstVal.SUPER_SERVICE_CLASS;
        } else {
            superServiceClass = config.getSuperServiceClass();
        }
        if (GeneratorUtils.isEmpty(config.getSuperServiceImplClass())) {
            superServiceImplClass = ConstVal.SUPER_SERVICE_IMPL_CLASS;
        } else {
            superServiceImplClass = config.getSuperServiceImplClass();
        }
        if (GeneratorUtils.isEmpty(config.getSuperMapperClass())) {
            superMapperClass = ConstVal.SUPER_MAPPER_CLASS;
        } else {
            superMapperClass = config.getSuperMapperClass();
        }
        superEntityClass = config.getSuperEntityClass();
        superControllerClass = config.getSuperControllerClass();
    }


    /**
     * 处理表对应的类名称
     *
     * @param tableList 表名称
     * @param strategy  命名策略
     * @param config    策略配置项
     * @return 补充完整信息后的表
     */
    private List<TableInfo> processTable(List<TableInfo> tableList, NamingStrategy strategy, StrategyConfig config) {
        String[] tablePrefix = config.getTablePrefix();
        for (TableInfo tableInfo : tableList) {
            String entityName;
            INameConvert nameConvert = strategyConfig.getNameConvert();
            if (null != nameConvert) {
                // 自定义处理实体名称
                entityName = nameConvert.entityNameConvert(tableInfo);
            } else {
                entityName = NamingStrategy.capitalFirst(processName(tableInfo.getName(), strategy, tablePrefix));
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
            // 检测导入包
            checkImportPackages(tableInfo);
        }
        return tableList;
    }

    /**
     * 检测导入包
     *
     * @param tableInfo ignore
     */
    private void checkImportPackages(TableInfo tableInfo) {
        if (GeneratorUtils.isNotEmpty(strategyConfig.getSuperEntityClass())) {
            // 自定义父类
            tableInfo.getImportPackages().add(strategyConfig.getSuperEntityClass());
        }
    }


    /**
     * 获取所有的数据库表信息
     */
    private List<TableInfo> getTablesInfo(StrategyConfig config) {
        boolean isInclude = (null != config.getInclude() && config.getInclude().length > 0);
        boolean isExclude = (null != config.getExclude() && config.getExclude().length > 0);
        if (isInclude && isExclude) {
            throw new RuntimeException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        }
        //所有的表信息
        List<TableInfo> tableList = new ArrayList<>();

        //需要反向生成或排除的表信息
        List<TableInfo> includeTableList = new ArrayList<>();
        List<TableInfo> excludeTableList = new ArrayList<>();

        //不存在的表名
        Set<String> notExistTables = new HashSet<>();
        try {
            String tablesSql = dbQuery.tablesSql();
            if (DbType.POSTGRE_SQL == dbQuery.dbType()) {
                String schema = dataSourceConfig.getSchemaName();
                if (schema == null) {
                    //pg 默认 schema=public
                    schema = "public";
                    dataSourceConfig.setSchemaName(schema);
                }
                tablesSql = String.format(tablesSql, schema);
            }
            if (DbType.DB2 == dbQuery.dbType()) {
                String schema = dataSourceConfig.getSchemaName();
                if (schema == null) {
                    //db2 默认 schema=current schema
                    schema = "current schema";
                    dataSourceConfig.setSchemaName(schema);
                }
                tablesSql = String.format(tablesSql, schema);
            }
            //oracle数据库表太多，出现最大游标错误
            else if (DbType.ORACLE == dbQuery.dbType()) {
                String schema = dataSourceConfig.getSchemaName();
                //oracle 默认 schema=username
                if (schema == null) {
                    schema = dataSourceConfig.getUsername().toUpperCase();
                    dataSourceConfig.setSchemaName(schema);
                }
                tablesSql = String.format(tablesSql, schema);
                if (isInclude) {
                    StringBuilder sb = new StringBuilder(tablesSql);
                    sb.append(" AND ").append(dbQuery.tableName()).append(" IN (");
                    Arrays.stream(config.getInclude()).forEach(tbname -> sb.append(StringPool.SINGLE_QUOTE).append(tbname.toUpperCase()).append("',"));
                    sb.replace(sb.length() - 1, sb.length(), StringPool.RIGHT_BRACKET);
                    tablesSql = sb.toString();
                } else if (isExclude) {
                    StringBuilder sb = new StringBuilder(tablesSql);
                    sb.append(" AND ").append(dbQuery.tableName()).append(" NOT IN (");
                    Arrays.stream(config.getExclude()).forEach(tbname -> sb.append(StringPool.SINGLE_QUOTE).append(tbname.toUpperCase()).append("',"));
                    sb.replace(sb.length() - 1, sb.length(), StringPool.RIGHT_BRACKET);
                    tablesSql = sb.toString();
                }
            }
            TableInfo tableInfo;
            try (PreparedStatement preparedStatement = connection.prepareStatement(tablesSql);
                 ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    String tableName = results.getString(dbQuery.tableName());
                    if (GeneratorUtils.isNotEmpty(tableName)) {
                        tableInfo = new TableInfo();
                        tableInfo.setName(tableName);

                        if (commentSupported) {
                            String tableComment = results.getString(dbQuery.tableComment());
                            if (config.isSkipView() && "VIEW".equals(tableComment)) {
                                // 跳过视图
                                continue;
                            }
                            tableInfo.setComment(tableComment);
                        }

                        if (isInclude) {
                            for (String includeTable : config.getInclude()) {
                                // 忽略大小写等于 或 正则 true
                                if (tableNameMatches(includeTable, tableName)) {
                                    includeTableList.add(tableInfo);
                                } else {
                                    notExistTables.add(includeTable);
                                }
                            }
                        } else if (isExclude) {
                            for (String excludeTable : config.getExclude()) {
                                // 忽略大小写等于 或 正则 true
                                if (tableNameMatches(excludeTable, tableName)) {
                                    excludeTableList.add(tableInfo);
                                } else {
                                    notExistTables.add(excludeTable);
                                }
                            }
                        }
                        tableList.add(tableInfo);
                    } else {
                        System.err.println("当前数据库为空！！！");
                    }
                }
            }
            // 将已经存在的表移除，获取配置中数据库不存在的表
            for (TableInfo tabInfo : tableList) {
                notExistTables.remove(tabInfo.getName());
            }
            if (notExistTables.size() > 0) {
                System.err.println("表 " + notExistTables + " 在数据库中不存在！！！");
            }

            // 需要反向生成的表信息
            if (isExclude) {
                tableList.removeAll(excludeTableList);
                includeTableList = tableList;
            }
            if (!isInclude && !isExclude) {
                includeTableList = tableList;
            }
            // 性能优化，只处理需执行表字段 github issues/219
            includeTableList.forEach(ti -> convertTableFields(ti, config));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return processTable(includeTableList, config.getNaming(), config);
    }


    /**
     * 表名匹配
     *
     * @param setTableName 设置表名
     * @param dbTableName  数据库表单
     * @return ignore
     */
    private boolean tableNameMatches(String setTableName, String dbTableName) {
        return setTableName.equalsIgnoreCase(dbTableName)
                || this.matches(setTableName, dbTableName);
    }

    /**
     * 正则表达式匹配
     *
     * @param regex 正则表达式字符串
     * @param input 要匹配的字符串
     * @return 如果 input 符合 regex 正则表达式格式, 返回true, 否则返回 false;
     */
    private boolean matches(String regex, String input) {
        if (null == regex || null == input) {
            return false;
        }
        return Pattern.matches(regex, input);
    }

    /**
     * 将字段信息与表信息关联
     *
     * @param tableInfo 表信息
     * @param config    命名策略
     * @return ignore
     */
    private TableInfo convertTableFields(TableInfo tableInfo, StrategyConfig config) {
        boolean haveId = false;
        List<TableField> fieldList = new ArrayList<>();
        List<TableField> commonFieldList = new ArrayList<>();
        DbType dbType = dbQuery.dbType();
        String tableName = tableInfo.getName();
        try {
            String tableFieldsSql = dbQuery.tableFieldsSql();
            Set<String> h2PkColumns = new HashSet<>();
//            if (DbType.POSTGRE_SQL == dbType) {
//                tableFieldsSql = String.format(tableFieldsSql, dataSourceConfig.getSchemaName(), tableName);
//            } else if (DbType.DB2 == dbType) {
//                tableFieldsSql = String.format(tableFieldsSql, dataSourceConfig.getSchemaName(), tableName);
//            } else if (DbType.ORACLE == dbType) {
//                tableName = tableName.toUpperCase();
//                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", dataSourceConfig.getSchemaName()), tableName);
//            } else if (DbType.H2 == dbType) {
//                tableName = tableName.toUpperCase();
//                try (PreparedStatement pkQueryStmt = connection.prepareStatement(String.format(H2Query.PK_QUERY_SQL, tableName));
//                     ResultSet pkResults = pkQueryStmt.executeQuery()) {
//                    while (pkResults.next()) {
//                        String primaryKey = pkResults.getString(dbQuery.fieldKey());
//                        if (Boolean.valueOf(primaryKey)) {
//                            h2PkColumns.add(pkResults.getString(dbQuery.fieldName()));
//                        }
//                    }
//                }
//                tableFieldsSql = String.format(tableFieldsSql, tableName);
//            } else {
//                tableFieldsSql = String.format(tableFieldsSql, tableName);
//            }
            tableFieldsSql = String.format(tableFieldsSql, tableName);
            try (PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql);
                 ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    TableField field = new TableField();
                    String columnName = results.getString(dbQuery.fieldName());
                    // 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
                    boolean isId;
                    if (DbType.H2 == dbType) {
                        isId = h2PkColumns.contains(columnName);
                    } else {
                        String key = results.getString(dbQuery.fieldKey());
                        if (DbType.DB2 == dbType || DbType.SQLITE == dbType) {
                            isId = GeneratorUtils.isNotEmpty(key) && "1".equals(key);
                        } else {
                            isId = GeneratorUtils.isNotEmpty(key) && "PRI".equals(key.toUpperCase());
                        }
                    }

                    // 处理ID
                    if (isId && !haveId) {
                        field.setKeyFlag(true);
                        if (DbType.H2 == dbType || DbType.SQLITE == dbType || dbQuery.isKeyIdentity(results)) {
                            field.setKeyIdentityFlag(true);
                        }
                        haveId = true;
                    } else {
                        field.setKeyFlag(false);
                    }
                    // 自定义字段查询
                    String[] fcs = dbQuery.fieldCustom();
                    if (null != fcs) {
                        Map<String, Object> customMap = new HashMap<>(fcs.length);
                        for (String fc : fcs) {
                            customMap.put(fc, results.getObject(fc));
                        }
                        field.setCustomMap(customMap);
                    }
                    // 处理其它信息
                    field.setName(columnName);
                    field.setType(results.getString(dbQuery.fieldType()));
                    INameConvert nameConvert = strategyConfig.getNameConvert();
                    if (null != nameConvert) {
                        field.setPropertyName(nameConvert.propertyNameConvert(field));
                    } else {
                        field.setPropertyName(strategyConfig, processName(field.getName(), config.getNaming()));
                    }
                    field.setColumnType(dataSourceConfig.getTypeConvert().processTypeConvert(field.getType()));
                    if (commentSupported) {
                        field.setComment(results.getString(dbQuery.fieldComment()));
                    }
                    if (strategyConfig.includeSuperEntityColumns(field.getName())) {
                        // 跳过公共字段
                        commonFieldList.add(field);
                        continue;
                    }
                    // 填充逻辑判断
//                    List<TableFill> tableFillList = getStrategyConfig().getTableFillList();
//                    if (null != tableFillList) {
//                        // 忽略大写字段问题
//                        tableFillList.stream().filter(tf -> tf.getFieldName().equalsIgnoreCase(field.getName()))
//                                .findFirst().ifPresent(tf -> field.setFill(tf.getFieldFill().name()));
//                    }
                    fieldList.add(field);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception：" + e.getMessage());
        }
        tableInfo.setFields(fieldList);
        tableInfo.setCommonFields(commonFieldList);
        return tableInfo;
    }


    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (GeneratorUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!GeneratorUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }


    /**
     * 连接父子包名
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    private String joinPackage(String parent, String subPackage) {
        if (GeneratorUtils.isEmpty(parent)) {
            return subPackage;
        }
        return parent + StringPool.DOT + subPackage;
    }


    /**
     * 处理字段名称
     *
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy) {
        return processName(name, strategy, strategyConfig.getFieldPrefix());
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


    public StrategyConfig getStrategyConfig() {
        return strategyConfig;
    }


    public ConfigBuilder setStrategyConfig(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
        return this;
    }


    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }


    public ConfigBuilder setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }


//    public InjectionConfig getInjectionConfig() {
//        return injectionConfig;
//    }
//
//
//    public ConfigBuilder setInjectionConfig(InjectionConfig injectionConfig) {
//        this.injectionConfig = injectionConfig;
//        return this;
//    }


    public SqlInfo getSqlInfo() {
        return sqlInfo;
    }

    public void setSqlInfo(SqlInfo sqlInfo) {
        this.sqlInfo = sqlInfo;
    }
}
