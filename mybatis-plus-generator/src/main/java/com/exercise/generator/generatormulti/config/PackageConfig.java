package com.exercise.generator.generatormulti.config;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 跟包相关的配置项
 */
@Data
@Accessors(chain = true)
public class PackageConfig {

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    private String parent = "";
    /**
     * 父包模块名
     */
    private String moduleName = null;
    /**
     * Entity包名
     */
    private String entity = "model.vo";
    /**
     * Service包名
     */
    private String service = "service";
    /**
     * Service Impl包名
     */
    private String serviceImpl = "service.impl";
    /**
     * Mapper包名
     */
    private String mapper = "mapper";
    /**
     * Mapper XML包名
     */
    private String xml = "mapper";
    /**
     * Controller包名
     */
    private String controller = "controller";
    /**
     * Controller包名
     */
    private String controllerReq = "model.vo.generator";
    /**
     * Controller包名
     */
    private String controllerRes = "model.vo.generator";
    /**
     * 分页请求基础类
     */
    private String PageRequest = "model";
    /**
     * 分页响应基础类
     */
    private String PageResponse = "model";

    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;
}
