
package com.exercise.generator.generatormulti.config;

import java.nio.charset.StandardCharsets;

/**
 * 定义常量
 */
public interface ConstVal {

    String MODULE_NAME = "ModuleName";

    String ENTITY = "Entity";
    String SERVICE = "Service";
    String SERVICE_IMPL = "ServiceImpl";
    String MAPPER = "Mapper";
    String XML = "Xml";
    String CONTROLLER = "Controller";
    String CONTROLLER_REQ = "ControllerReq";
    String CONTROLLER_RES = "ControllerRes";
    String PAGE_REQUEST = "PageRequest";
    String PAGE_RESPONSE = "PageResponse";

    String ENTITY_PATH = "entity_path";
    String SERVICE_PATH = "service_path";
    String SERVICE_IMPL_PATH = "service_impl_path";
    String MAPPER_PATH = "mapper_path";
    String XML_PATH = "xml_path";
    String CONTROLLER_PATH = "controller_path";
    String CONTROLLER_REQ_PATH = "controller_req_path";
    String CONTROLLER_RES_PATH = "controller_res_path";
    String PAGE_REQUEST_PATH = "page_request_path";
    String PAGE_RESPONSE_PATH = "page_response_path";

    String JAVA_TMPDIR = "java.io.tmpdir";
    String UTF8 = StandardCharsets.UTF_8.name();
    String UNDERLINE = "_";

    String JAVA_SUFFIX = ".java";
    String KT_SUFFIX = ".kt";
    String XML_SUFFIX = ".xml";

    String TEMPLATE_ENTITY_JAVA = "/templateBySQL/entity.java";
    String TEMPLATE_ENTITY_KT = "/templateBySQL/entity.kt";
    String TEMPLATE_MAPPER = "/templateBySQL/mapper.java";
    String TEMPLATE_XML = "/templateBySQL/mapper.xml";
    String TEMPLATE_SERVICE = "/templateBySQL/service.java";
    String TEMPLATE_SERVICE_IMPL = "/templateBySQL/serviceImpl.java";
    String TEMPLATE_CONTROLLER = "/templateBySQL/controller.java";
    String TEMPLATE_CONTROLLER_REQ = "/templateBySQL/controllerReq.java";
    String TEMPLATE_CONTROLLER_RES = "/templateBySQL/controllerRes.java";
    String TEMPLATE_PAGE_REQUEST = "/templateBySQL/pageRequest.java";
    String TEMPLATE_PAGE_RESPONSE = "/templateBySQL/pageResponse.java";


    String VM_LOAD_PATH_KEY = "file.resource.loader.class";
    String VM_LOAD_PATH_VALUE = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    String SUPER_MAPPER_CLASS = "com.baomidou.mybatisplus.core.mapper.BaseMapper";
    String SUPER_SERVICE_CLASS = "com.baomidou.mybatisplus.extension.service.IService";
    String SUPER_SERVICE_IMPL_CLASS = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";

}
