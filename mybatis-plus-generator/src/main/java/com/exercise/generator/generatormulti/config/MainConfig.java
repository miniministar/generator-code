package com.exercise.generator.generatormulti.config;

import lombok.Data;

/**
 * @description:
 * @author: ZL
 * @date: 2019-07-02 10:39
 */
@Data
public class MainConfig {
    /**
     * sql语句
     */
    private String sql;
    /**
     * 生成信息名称
     */
    private String baseName;
    /**
     * HTTP请求方法
     */
    private String requestMethod = "POST";
    /**
     * 请求地址
     */
    private String requestUrl;
    /**
     * 是否分页查询
     */
    private boolean pageFlag = true;
    /**
     * 说明
     */
    private String description;
    /**
     * 版本号
     */
    private String version;
    /**
     * 查询参数
     */
    private String[] params;

}
