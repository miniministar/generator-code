package ${package.Mapper};

import ${package.ControllerRes}.${table.controllerResName};
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * ${table.comment!} Mapper接口
 *
 * @author ${author}
 * @since ${date}
 */
@Repository
@Mapper
public interface ${table.mapperName} {

<#assign i = 0>
    List<${table.controllerResName}> ${table.controllerResName?uncap_first?replace("get","select")}List(<#list table.fields as field><#if field.queryFlag?string("true","flase") == "true"><#if i != 0>, </#if>@Param("${field.propertyName}") ${field.propertyType} ${field.propertyName}<#assign i = i + 1></#if></#list>);

}
