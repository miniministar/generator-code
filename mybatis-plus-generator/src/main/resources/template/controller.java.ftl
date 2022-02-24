<#assign basePath = "">
<#assign entityPath = entity?replace("Entity","")>
<#if entity?index_of("S") == 0 ><#assign basePath = "/system/" + entityPath ?substring(1)?uncap_first>
<#elseif entity?index_of("P") == 0 ><#assign basePath = "/platform/" + entityPath ?substring(1)?uncap_first>
<#elseif entity?index_of("T") == 0 ><#assign basePath = "/tenant/" + entityPath ?substring(1)?uncap_first></#if>
package ${package.Controller};

import cn.com.hopson.pms.common.annotation.GetLog;
import ${package.Entity?replace(".entity","")}.vo.${entityPath?lower_case}.Get${entityPath}ReqVo;
import ${package.Entity?replace(".entity","")}.vo.${entityPath?lower_case}.Get${entityPath}ResVo;
import ${package.Entity?replace(".entity","")}.vo.${entityPath?lower_case}.Add${entityPath}ReqVo;
import ${package.Entity?replace(".entity","")}.vo.${entityPath?lower_case}.Modify${entityPath}ReqVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
<#if swagger2>
import io.swagger.annotations.*;
</#if>
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ${table.comment!} 前端控制器
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@Slf4j
<#if kotlin>
    class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>

    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

    @ApiOperation(value = "${table.comment!}-列表查询",
            httpMethod = "GET",
            response = Get${entityPath}ResVo.class,
            tags = "${table.comment!}管理",
            extensions = @Extension(properties = {@ExtensionProperty(name = "version", value = "1.0.0")}))
    @GetMapping("${basePath}")
    public ResponseEntity get${entityPath}List(Get${entityPath}ReqVo req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        LambdaQueryWrapper<${entity}> queryWrapper = new LambdaQueryWrapper<>();
<#-- --------- BEGIN 查询参数赋值  ---------->
<#list table.fields as field>
    <#if field.keyFlag == false>
        <#if field.propertyType=="String">
        if (StringUtils.isNotBlank(req.get${field.propertyName?cap_first}())) {
            queryWrapper.like(${entity}::get${field.propertyName?cap_first}, req.get${field.propertyName?cap_first}());
        }
        <#else>
        if (req.get${field.propertyName?cap_first}() != null) {
            queryWrapper.eq(${entity}::get${field.propertyName?cap_first}, req.get${field.propertyName?cap_first}());
        }
        </#if>
    </#if>
</#list>
<#------------ END 查询参数赋值  ---------->
        List<${entity}> ${entity?uncap_first}List = ${table.serviceName?uncap_first}.list(queryWrapper);
        PageSerializable<${entity}> pageInfo = new PageSerializable<>(${entity?uncap_first}List);
        List<Get${entityPath}ResVo> get${entityPath}ResVoList = Lists.newArrayList();
        for (${entity} item : ${entity?uncap_first}List) {
            get${entityPath}ResVoList.add(${table.serviceName?uncap_first}.get${entityPath}ResVo(item));
        }
        PageSerializable<Get${entityPath}ResVo> pageRes = new PageSerializable<>(get${entityPath}ResVoList);
        pageRes.setTotal(pageInfo.getTotal());
        return new ResponseEntity<>(pageRes, HttpStatus.OK);
    }

    @ApiOperation(value = "${table.comment!}-新增",
            httpMethod = "POST",
            response = R.class,
            tags = "${table.comment!}管理",
            extensions = @Extension(properties = {@ExtensionProperty(name = "version", value = "1.0.0")}))
    @PostMapping("${basePath}")
    public ResponseEntity add${entityPath}(@Validated @RequestBody Add${entityPath}ReqVo req) {
        ${table.serviceName?uncap_first}.add${entityPath}(req);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "${table.comment!}-编辑",
            httpMethod = "PUT",
            response = R.class,
            tags = "${table.comment!}管理",
            extensions = @Extension(properties = {@ExtensionProperty(name = "version", value = "1.0.0")}))
    @PutMapping("${basePath}")
    public ResponseEntity modify${entityPath}(@Validated @RequestBody Modify${entityPath}ReqVo req) {
        ${table.serviceName?uncap_first}.modify${entityPath}(req);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "${table.comment!}-批量启用",
            httpMethod = "POST",
            response = R.class,
            tags = "${table.comment!}管理",
            extensions = @Extension(properties = {@ExtensionProperty(name = "version", value = "1.0.0")}))
    @PostMapping("${basePath}/batchEnable")
    public ResponseEntity batchEnable${entityPath}(@RequestBody List<Integer> idList) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", idList);
        List<${entity}> ${entity?uncap_first}List = ${table.serviceName?uncap_first}.list(queryWrapper);
        ${entity?uncap_first}List.forEach(item -> item.setStatus(1));
        ${table.serviceName?uncap_first}.updateBatchById(${entity?uncap_first}List);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "${table.comment!}-批量禁用",
            httpMethod = "POST",
            response = R.class,
            tags = "${table.comment!}管理",
            extensions = @Extension(properties = {@ExtensionProperty(name = "version", value = "1.0.0")}))
    @PostMapping("${basePath}/batchDisable")
    public ResponseEntity batchDisable${entityPath}(@RequestBody List<Integer> idList) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", idList);
        List<${entity}> ${entity?uncap_first}List = ${table.serviceName?uncap_first}.list(queryWrapper);
        ${entity?uncap_first}List.forEach(item -> item.setStatus(0));
        ${table.serviceName?uncap_first}.updateBatchById(${entity?uncap_first}List);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "${table.comment!}-批量删除",
            httpMethod = "POST",
            response = R.class,
            tags = "${table.comment!}管理",
            extensions = @Extension(properties = {@ExtensionProperty(name = "version", value = "1.0.0")}))
    @PostMapping("${basePath}/batchDelete")
    public ResponseEntity batchDelete${entityPath}(@RequestBody List<Integer> idList) {
        ${table.serviceName?uncap_first}.removeByIds(idList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "${table.comment!}-详情查询",
            httpMethod = "GET",
            response = Get${entityPath}ResVo.class,
            tags = "${table.comment!}管理",
            extensions = @Extension(properties = {@ExtensionProperty(name = "version", value = "1.0.0")}))
    @GetMapping("${basePath}/{id}")
    public ResponseEntity get${entityPath}(@PathVariable("id") Integer id) {
        Get${entityPath}ResVo get${entityPath}ResVo = ${table.serviceName?uncap_first}.get${entityPath}ResVo(id);
        return new ResponseEntity<>(get${entityPath}ResVo, HttpStatus.OK);
    }

}
</#if>
