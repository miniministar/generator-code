<#assign entityPath = entity?replace("Entity","")>
package ${package.Entity?replace(".entity","") + ".vo." + entityPath?replace("Sys","")?lower_case};

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.io.Serializable;
<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
</#if>

/**
 * <p>
 * ${table.comment!}-${entityPath}VO
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */

@Data
<#if swagger2>
@ApiModel(value = "${entityPath}VO对象", description = "${table.comment!}-${entityPath}VO")
</#if>
public class ${entityPath}VO implements Serializable {
    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if swagger2>
    @ApiModelProperty(value = "${field.comment}")
    </#if>
    <#if (field.propertyType == "LocalDateTime")>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    <#if (field.propertyType == "LocalDate")>
    @JsonFormat(pattern = "yyyy-MM-dd")
    </#if>
    private ${field.propertyType} ${field.propertyName};

</#list>
}
