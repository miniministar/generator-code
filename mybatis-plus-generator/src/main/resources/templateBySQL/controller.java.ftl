package ${package.Controller};

import cn.com.hopson.common.base.R;
<#if swagger2>
import io.swagger.annotations.*;
</#if>
<#if table.pageFlag>
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
</#if>
import ${package.Service}.${table.serviceName};
import ${package.ControllerReq}.${table.controllerReqName};
import ${package.ControllerRes}.${table.controllerResName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
<#if !restControllerStyle>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import java.util.List;

/**
 * ${table.comment!}控制器
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

<#assign i = 0>
<#if swagger2>
    @ApiOperation(value = "${table.comment!}",
            httpMethod = "POST",
            response = ${table.controllerResName}.class,
            tags = "",
            extensions = @Extension(properties = {@ExtensionProperty(name = "v", value = "${table.version}")}
            ))
</#if>
    @PostMapping(value = "${table.requestUrl}")
    public R get${entity}(@RequestBody ${table.controllerReqName} req) {
<#if table.pageFlag>
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
</#if>
        List<${table.controllerResName}> list = ${table.serviceName?uncap_first}.${table.controllerResName?uncap_first}List(<#list table.fields as field><#if field.queryFlag?string("true","flase") == "true"><#if i != 0>, </#if>req.get${field.propertyName?cap_first}()<#assign i = i + 1></#if></#list>);
        PageSerializable<${table.controllerResName}> pageRes = new PageSerializable<>(list);
        return new R(pageRes);
    }
}
