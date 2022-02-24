package ${package.Service};
<#assign entityPath = entity?replace("Entity","")>
import ${package.Entity}.${entity};
import ${package.Entity?replace(".entity","")}.vo.${entityPath?lower_case}.Get${entityPath}ResVo;
import ${package.Entity?replace(".entity","")}.vo.${entityPath?lower_case}.Add${entityPath}ReqVo;
import ${package.Entity?replace(".entity","")}.vo.${entityPath?lower_case}.Modify${entityPath}ReqVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import ${superServiceClassPackage};

import java.util.List;

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    Get${entityPath}ResVo get${entityPath}ResVo(${entity} ${entity?uncap_first});

    boolean add${entityPath}(Add${entityPath}ReqVo addReq);

    boolean modify${entityPath}(Modify${entityPath}ReqVo modifyReq);

    Get${entityPath}ResVo get${entityPath}ResVo(Integer id);
}
</#if>
