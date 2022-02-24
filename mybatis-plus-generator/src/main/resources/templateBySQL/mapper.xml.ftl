<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >

<mapper namespace="${package.Mapper}.${table.mapperName}">
<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>
</#if>
    <resultMap id="BaseResultMap" type="${package.ControllerRes}.${table.controllerResName}">
        <#list table.fields as field>
            <#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.nameAlias}" property="${field.propertyName}"/>
            </#if>
        </#list>
    </resultMap>
    <select id="${table.controllerResName?uncap_first?replace("get","select")}List" resultMap="BaseResultMap">
<#assign i = 0>
<#assign j = 0>
        SELECT
        ${sqlInfo.querySQL}
        FROM
        ${sqlInfo.tableSQL}
        <#if sqlInfo.whereSQL?? && sqlInfo.whereSQL!="">
        WHERE ${sqlInfo.whereSQL}
<#assign i = 1>
<#assign j = 1>
        </#if>
        <#list table.fields as field>
            <#if field.queryFlag?string("true", "flase") == "true">
                <#if field.propertyType == "String">
        <if test="${field.propertyName}!=null and ${field.propertyName}!=''">
            <#if j == 0>WHERE</#if><#if i == 1>AND</#if> ${field.tableAlias}.${field.name} like CONCAT('%',${'#\{' + field.propertyName + ',jdbcType=' + field.propertyType?upper_case + '}'},'%'
<#assign i = 1>
<#assign j = 1>
        </if>
                <#else>
        <if test="${field.propertyName}!=null">
            <#if j == 0>WHERE</#if><#if i == 1>AND</#if> ${field.tableAlias}.${field.name} = ${'#\{' + field.propertyName + ',jdbcType=' + field.propertyType?upper_case + '}'}
<#assign i = 1>
<#assign j = 1>
        </if>
                </#if>
            </#if>
        </#list>
        <#if sqlInfo.groupBySQL?? && sqlInfo.groupBySQL!="">
        GROUP BY ${sqlInfo.groupBySQL}
        </#if>
        <#if sqlInfo.orderBySQL?? && sqlInfo.orderBySQL!="">
        ORDER BY ${sqlInfo.orderBySQL}
        </#if>
    </select>
</mapper>
