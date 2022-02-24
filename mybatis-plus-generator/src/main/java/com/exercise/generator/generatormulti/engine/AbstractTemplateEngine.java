package com.exercise.generator.generatormulti.engine;

import com.exercise.generator.generatormulti.config.ConstVal;
import com.exercise.generator.generatormulti.config.GlobalConfig;
import com.exercise.generator.generatormulti.config.StringPool;
import com.exercise.generator.generatormulti.config.TemplateConfig;
import com.exercise.generator.generatormulti.config.builder.ConfigBuilder;
import com.exercise.generator.generatormulti.config.rules.FileType;
import com.exercise.generator.generatormulti.po.TableInfo;
import com.exercise.generator.generatormulti.util.GeneratorUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTemplateEngine {

    /**
     * 配置信息
     */
    private ConfigBuilder configBuilder;


    /**
     * 模板引擎初始化
     */
    public AbstractTemplateEngine init(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
        return this;
    }


    /**
     * 输出 java xml 文件
     */
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
            for (TableInfo tableInfo : tableInfoList) {
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
                TemplateConfig template = getConfigBuilder().getTemplate();
                // Entity.java
                String entityName = tableInfo.getEntityName();
//                if (null != entityName && null != pathInfo.get(ConstVal.ENTITY_PATH)) {
//                    String entityFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName);
//                    if (isCreate(FileType.ENTITY, entityFile)) {
//                        writer(objectMap, templateFilePath(template.getEntity(getConfigBuilder().getGlobalConfig().isKotlin())), entityFile);
//                    }
//                }
                // MpMapper.java
                if (null != tableInfo.getMapperName() && null != pathInfo.get(ConstVal.MAPPER_PATH)) {
                    String mapperFile = String.format((pathInfo.get(ConstVal.MAPPER_PATH) + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.MAPPER, mapperFile)) {
                        writer(objectMap, templateFilePath(template.getMapper()), mapperFile);
                    }
                }
                // MpMapper.xml
                if (null != tableInfo.getXmlName() && null != pathInfo.get(ConstVal.XML_PATH)) {
                    String xmlFile = String.format((pathInfo.get(ConstVal.XML_PATH) + File.separator + tableInfo.getXmlName() + ConstVal.XML_SUFFIX), entityName);
                    if (isCreate(FileType.XML, xmlFile)) {
                        writer(objectMap, templateFilePath(template.getXml()), xmlFile);
                    }
                }
                // IMpService.java
                if (null != tableInfo.getServiceName() && null != pathInfo.get(ConstVal.SERVICE_PATH)) {
                    String serviceFile = String.format((pathInfo.get(ConstVal.SERVICE_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE, serviceFile)) {
                        writer(objectMap, templateFilePath(template.getService()), serviceFile);
                    }
                }
                // MpServiceImpl.java
                if (null != tableInfo.getServiceImplName() && null != pathInfo.get(ConstVal.SERVICE_IMPL_PATH)) {
                    String implFile = String.format((pathInfo.get(ConstVal.SERVICE_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE_IMPL, implFile)) {
                        writer(objectMap, templateFilePath(template.getServiceImpl()), implFile);
                    }
                }
                // MpController.java
                if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_PATH)) {
                    String controllerFile = String.format((pathInfo.get(ConstVal.CONTROLLER_PATH) + File.separator + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.CONTROLLER, controllerFile)) {
                        writer(objectMap, templateFilePath(template.getController()), controllerFile);
                    }
                }
                // MpControllerReq.java
                if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_REQ_PATH)) {
                    String controllerReqFile = String.format((pathInfo.get(ConstVal.CONTROLLER_REQ_PATH) + File.separator + tableInfo.getControllerReqName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.OTHER, controllerReqFile)) {
                        writer(objectMap, templateFilePath(template.getControllerReq()), controllerReqFile);
                    }
                }
                // MpControllerRes.java
                if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_RES_PATH)) {
                    String controllerResFile = String.format((pathInfo.get(ConstVal.CONTROLLER_RES_PATH) + File.separator + tableInfo.getControllerResName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.OTHER, controllerResFile)) {
                        writer(objectMap, templateFilePath(template.getControllerRes()), controllerResFile);
                    }
                }
//                // PageRequest.java
//                if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.PAGE_REQUEST_PATH)) {
//                    String pageRequestFile = String.format((pathInfo.get(ConstVal.PAGE_REQUEST_PATH) + File.separator + ConstVal.PAGE_REQUEST + suffixJavaOrKt()), entityName);
//                    if (isCreate(FileType.OTHER, pageRequestFile)) {
//                        writer(objectMap, templateFilePath(template.getPageRequest()), pageRequestFile);
//                    }
//                }
//                // PageResponse.java
//                if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.PAGE_RESPONSE_PATH)) {
//                    String pageResponseFile = String.format((pathInfo.get(ConstVal.PAGE_RESPONSE_PATH) + File.separator + ConstVal.PAGE_RESPONSE + suffixJavaOrKt()), entityName);
//                    if (isCreate(FileType.OTHER, pageResponseFile)) {
//                        writer(objectMap, templateFilePath(template.getPageResponse()), pageResponseFile);
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


    /**
     * 将模板转化成为文件
     *
     * @param objectMap    渲染对象 MAP 信息
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     */
    public abstract void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception;

    /**
     * 处理输出目录
     */
    public AbstractTemplateEngine mkdirs() {
        getConfigBuilder().getPathInfo().forEach((key, value) -> {
            File dir = new File(value);
            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    System.out.println("创建目录： [" + value + "]");
                }
            }
        });
        return this;
    }


    /**
     * 打开输出目录
     */
    public void open() {
        String outDir = getConfigBuilder().getGlobalConfig().getOutputDir();
        if (getConfigBuilder().getGlobalConfig().isOpen()
                && GeneratorUtils.isNotEmpty(outDir)) {
            try {
                String osName = System.getProperty("os.name");
                if (osName != null) {
                    if (osName.contains("Mac")) {
                        Runtime.getRuntime().exec("open " + outDir);
                    } else if (osName.contains("Windows")) {
                        Runtime.getRuntime().exec("cmd /c start " + outDir);
                    } else {
                        System.out.println("文件输出目录:" + outDir);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 渲染对象 MAP 信息
     *
     * @param tableInfo 表信息对象
     * @return ignore
     */
    public Map<String, Object> getObjectMap(TableInfo tableInfo) {
        Map<String, Object> objectMap = new HashMap<>(30);
        ConfigBuilder config = getConfigBuilder();
        if (config.getStrategyConfig().isControllerMappingHyphenStyle()) {
            objectMap.put("controllerMappingHyphenStyle", config.getStrategyConfig().isControllerMappingHyphenStyle());
            objectMap.put("controllerMappingHyphen", GeneratorUtils.camelToHyphen(tableInfo.getEntityPath()));
        }
        objectMap.put("restControllerStyle", config.getStrategyConfig().isRestControllerStyle());
        objectMap.put("config", config);
        objectMap.put("package", config.getPackageInfo());
        GlobalConfig globalConfig = config.getGlobalConfig();
        objectMap.put("author", globalConfig.getAuthor());
//        objectMap.put("idType", globalConfig.getIdType() == null ? null : globalConfig.getIdType().toString());
        objectMap.put("logicDeleteFieldName", config.getStrategyConfig().getLogicDeleteFieldName());
        objectMap.put("versionFieldName", config.getStrategyConfig().getVersionFieldName());
        objectMap.put("activeRecord", globalConfig.isActiveRecord());
        objectMap.put("kotlin", globalConfig.isKotlin());
        objectMap.put("swagger2", globalConfig.isSwagger2());
        objectMap.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        objectMap.put("table", tableInfo);
        objectMap.put("enableCache", globalConfig.isEnableCache());
//        objectMap.put("baseResultMap", globalConfig.isBaseResultMap());
        objectMap.put("baseColumnList", globalConfig.isBaseColumnList());
        objectMap.put("entity", tableInfo.getEntityName());
        objectMap.put("entitySerialVersionUID", config.getStrategyConfig().isEntitySerialVersionUID());
        objectMap.put("entityColumnConstant", config.getStrategyConfig().isEntityColumnConstant());
        objectMap.put("entityBuilderModel", config.getStrategyConfig().isEntityBuilderModel());
        objectMap.put("entityLombokModel", config.getStrategyConfig().isEntityLombokModel());
        objectMap.put("entityBooleanColumnRemoveIsPrefix", config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix());
        objectMap.put("superEntityClass", getSuperClassName(config.getSuperEntityClass()));
        objectMap.put("superMapperClassPackage", config.getSuperMapperClass());
        objectMap.put("superMapperClass", getSuperClassName(config.getSuperMapperClass()));
        objectMap.put("superServiceClassPackage", config.getSuperServiceClass());
        objectMap.put("superServiceClass", getSuperClassName(config.getSuperServiceClass()));
        objectMap.put("superServiceImplClassPackage", config.getSuperServiceImplClass());
        objectMap.put("superServiceImplClass", getSuperClassName(config.getSuperServiceImplClass()));
        objectMap.put("superControllerClassPackage", config.getSuperControllerClass());
        objectMap.put("superControllerClass", getSuperClassName(config.getSuperControllerClass()));
        objectMap.put("sqlInfo", config.getSqlInfo());
        return objectMap;
    }


    /**
     * 获取类名
     *
     * @param classPath ignore
     * @return ignore
     */
    private String getSuperClassName(String classPath) {
        if (GeneratorUtils.isEmpty(classPath)) {
            return null;
        }
        return classPath.substring(classPath.lastIndexOf(StringPool.DOT) + 1);
    }


    /**
     * 模板真实文件路径
     *
     * @param filePath 文件路径
     * @return ignore
     */
    public abstract String templateFilePath(String filePath);


    /**
     * 检测文件是否存在
     *
     * @return 文件是否存在
     */
    protected boolean isCreate(FileType fileType, String filePath) {
        ConfigBuilder cb = getConfigBuilder();
        // 自定义判断
//        InjectionConfig ic = cb.getInjectionConfig();
//        if (null != ic && null != ic.getFileCreate()) {
//            return ic.getFileCreate().isCreate(cb, fileType, filePath);
//        }
        // 全局判断【默认】
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist) {
            file.getParentFile().mkdir();
        }
        return !exist || getConfigBuilder().getGlobalConfig().isFileOverride();
    }

    /**
     * 文件后缀
     */
    protected String suffixJavaOrKt() {
        return getConfigBuilder().getGlobalConfig().isKotlin() ? ConstVal.KT_SUFFIX : ConstVal.JAVA_SUFFIX;
    }


    public ConfigBuilder getConfigBuilder() {
        return configBuilder;
    }

    public AbstractTemplateEngine setConfigBuilder(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
        return this;
    }
}
