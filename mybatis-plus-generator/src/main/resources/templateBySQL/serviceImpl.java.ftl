package ${package.ServiceImpl};

import ${package.ControllerRes}.${table.controllerResName};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ${table.comment!} 服务实现类
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${table.serviceImplName} implements ${table.serviceName} {
<#assign i = 0>
    @Autowired
    protected ${table.mapperName} ${table.mapperName?uncap_first};

    @Override
    public List<${table.controllerResName}> ${table.controllerResName?uncap_first}List(<#list table.fields as field><#if field.queryFlag?string("true","flase") == "true"><#if i != 0>, </#if>${field.propertyType} ${field.propertyName}<#assign i = i + 1></#if></#list>) {
<#assign i = 0>
        return ${table.mapperName?uncap_first}.${table.controllerResName?uncap_first?replace("get","select")}List(<#list table.fields as field><#if field.queryFlag?string("true","flase") == "true"><#if i != 0>, </#if>${field.propertyName}<#assign i = i + 1></#if></#list>);
    }
}
