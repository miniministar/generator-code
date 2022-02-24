package ${package.ControllerRes};

<#list table.importPackages as pkg>
import ${pkg};
</#list>
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
 * ${table.comment!}响应参数
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
@ApiModel(value = "${table.comment!}响应参数", description = "${table.comment!}")
</#if>
<#if table.pageFlag>
public class ${table.controllerResName} {
<#else>
    <#if superEntityClass??>
public class ${table.controllerResName} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
    <#elseif activeRecord>
public class ${table.controllerResName} extends Model<${entity}> {
    <#else>
public class ${table.controllerResName} {
    </#if>
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.comment!?length gt 0>
        <#if swagger2>
    @ApiModelProperty(value = "${field.comment}")
        <#else>
            /**
            * ${field.comment}
            */
        </#if>
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->
}