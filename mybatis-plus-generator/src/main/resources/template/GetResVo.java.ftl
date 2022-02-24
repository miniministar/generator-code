<#assign entityPath = entity?replace("Entity","")>
package ${package.Entity?replace(".entity","") + ".vo." + entityPath?lower_case};

import java.time.LocalDateTime;
import java.io.Serializable;

<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.experimental.Accessors;
</#if>

/**
 * <p>
 * ${table.comment!}-Get${entityPath}ResVo
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */

@Data
@Accessors(chain = true)
<#if swagger2>
@ApiModel(value = "Get${entityPath}ResVo对象", description = "${table.comment!}-Get${entityPath}ResVo")
</#if>
public class Get${entityPath}ResVo implements Serializable {
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if swagger2>
    @ApiModelProperty(value = "${field.comment}")
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
}
