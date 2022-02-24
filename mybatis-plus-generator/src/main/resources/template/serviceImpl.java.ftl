package ${package.ServiceImpl};
<#assign entityPath = entity?replace("Entity","")>
import ${package.Entity}.${entity};
import ${package.Entity?replace(".entity","")}.vo.${entityPath?lower_case}.Get${entityPath}ResVo;
import ${package.Entity?replace(".entity","")}.vo.${entityPath?lower_case}.Add${entityPath}ReqVo;
import ${package.Entity?replace(".entity","")}.vo.${entityPath?lower_case}.Modify${entityPath}ReqVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    public Get${entityPath}ResVo get${entityPath}ResVo(${entity} ${entity?uncap_first}) {
        return this.setVo(${entity?uncap_first});
    }

    @Override
    public boolean add${entityPath}(Add${entityPath}ReqVo addReq) {
        ${entity} ${entity?uncap_first} = new ${entity}();
<#list table.fields as field>
        <#if field.propertyName!="isDelete" && field.propertyName!="createDt" && field.propertyName!="createMan" && field.propertyName!="updateDt" && field.propertyName!="updateMan">
        ${entity?uncap_first}.set${field.propertyName?cap_first}(addReq.get${field.propertyName?cap_first}());
        </#if>
</#list>
        return this.save(${entity?uncap_first});
    }

    @Override
    public boolean modify${entityPath}(Modify${entityPath}ReqVo modifyReq) {
        ${entity} ${entity?uncap_first} = this.getById(modifyReq.getId());
<#list table.fields as field>
        <#if field.propertyName!="isDelete" && field.propertyName!="createDt" && field.propertyName!="createMan" && field.propertyName!="updateDt" && field.propertyName!="updateMan">
        ${entity?uncap_first}.set${field.propertyName?cap_first}(modifyReq.get${field.propertyName?cap_first}());
        </#if>
</#list>
        return this.updateById(${entity?uncap_first});
    }

    @Override
    public Get${entityPath}ResVo get${entityPath}ResVo(Integer id) {
        ${entity} ${entity?uncap_first} = this.getById(id);
        return this.setVo(${entity?uncap_first});
    }

    private Get${entityPath}ResVo setVo(${entity} ${entity?uncap_first}) {
        Get${entityPath}ResVo vo = new Get${entityPath}ResVo();
<#list table.fields as field>
        vo.set${field.propertyName?cap_first}(${entity?uncap_first}.get${field.propertyName?cap_first}());
</#list>
        return vo;
    }

}
</#if>
