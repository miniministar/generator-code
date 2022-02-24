package ${package.ControllerReq};

import java.io.Serializable;
<#list table.importPackages as pkg>
import ${pkg};
</#list>
<#if table.pageFlag>
import ${package.Entity?replace(".entity","")}.PageRequestVo;
</#if>
<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>

/**
 * ${table.comment!}请求参数
 *
 * @author ${author}
 * @date ${date}
 */
<#if entityLombokModel>
@Data
@Accessors(chain = true)
 <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
 <#else>
@EqualsAndHashCode(callSuper = true)
 </#if>
</#if>
<#if swagger2>
@ApiModel(value = "${table.comment!}请求参数", description = "${table.comment!}")
</#if>
<#if table.pageFlag>
public class ${table.controllerReqName} extends PageRequestVo implements Serializable {
<#else>
    <#if superEntityClass??>
public class ${table.controllerReqName} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
    <#elseif activeRecord>
public class ${table.controllerReqName} extends Model<${entity}> {
    <#else>
public class ${table.controllerReqName} {
    </#if>
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
 <#if field.comment!?length gt 0 && field.queryFlag?string("true","flase") == "true">
  <#if swagger2>
    @ApiModelProperty(value = "${field.comment}")
  <#else>
    /**
    * ${field.comment}
    */
  </#if>
    private ${field.propertyType} ${field.propertyName};
 </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}