<#assign entityPath = entity?replace("Entity","")>
package ${package.Entity?replace(".entity","") + ".vo." + entityPath?lower_case};

import java.time.LocalDateTime;
import java.io.Serializable;

import ${package.Entity?replace(".entity","")}.vo.PageRequestVo;
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
 * <p>
 * ${table.comment!}-Get${entityPath}ReqVo
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
<#if swagger2>
@ApiModel(value = "Get${entityPath}ReqVo对象", description = "${table.comment!}-Get${entityPath}ReqVo")
</#if>
public class Get${entityPath}ReqVo extends PageRequestVo implements Serializable {
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if swagger2>
    @ApiModelProperty(value = "${field.comment}")
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
}
