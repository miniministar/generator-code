package ${package.Service};

import ${package.ControllerRes}.${table.controllerResName};

import java.util.List;

/**
 * ${table.comment!} 接口
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${table.serviceName} {
<#assign i = 0>

    List<${table.controllerResName}> ${table.controllerResName?uncap_first}List(<#list table.fields as field><#if field.queryFlag?string("true","flase") == "true"><#if i != 0>, </#if>${field.propertyType} ${field.propertyName}<#assign i = i + 1></#if></#list>);

}

