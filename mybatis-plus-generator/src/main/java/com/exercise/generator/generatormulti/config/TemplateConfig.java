package com.exercise.generator.generatormulti.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 模板路径配置项
 */
@Data
@Accessors(chain = true)
public class TemplateConfig {

    @Getter(AccessLevel.NONE)
    private String entity = ConstVal.TEMPLATE_ENTITY_JAVA;

    private String entityKt = ConstVal.TEMPLATE_ENTITY_KT;

    private String service = ConstVal.TEMPLATE_SERVICE;

    private String serviceImpl = ConstVal.TEMPLATE_SERVICE_IMPL;

    private String mapper = ConstVal.TEMPLATE_MAPPER;

    private String xml = ConstVal.TEMPLATE_XML;

    private String controller = ConstVal.TEMPLATE_CONTROLLER;

    private String controllerReq = ConstVal.TEMPLATE_CONTROLLER_REQ;

    private String controllerRes = ConstVal.TEMPLATE_CONTROLLER_RES;

    private String pageRequest = ConstVal.TEMPLATE_PAGE_REQUEST;

    private String pageResponse = ConstVal.TEMPLATE_PAGE_RESPONSE;

    public String getEntity(boolean kotlin) {
        return kotlin ? entityKt : entity;
    }
}
