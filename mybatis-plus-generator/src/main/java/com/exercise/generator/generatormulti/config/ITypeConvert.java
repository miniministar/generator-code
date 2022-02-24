package com.exercise.generator.generatormulti.config;


import com.exercise.generator.generatormulti.config.rules.IColumnType;

/**
 * 数据库字段类型转换
 *
 * @author hubin
 * @since 2017-01-20
 */
public interface ITypeConvert {


    /**
     * 执行类型转换
     *
     * @param fieldType 字段类型
     * @return ignore
     */
    IColumnType processTypeConvert(String fieldType);
}
