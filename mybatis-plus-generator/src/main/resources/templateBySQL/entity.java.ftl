package ${package.Entity};

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
 * ${entity}
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
@ApiModel(value = "${entity}对象", description = "${table.comment!}")
</#if>
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class ${entity} extends Model<${entity}> {
<#else>
public class ${entity} {
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