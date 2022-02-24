package com.exercise.generator.generatormulti.po;

import lombok.Data;

/**
 * @description:
 * @author: ZL
 * @date: 2019-06-19 14:24
 */
@Data
public class QueryPart {
    /**
     * 表别名
     */
    private String tableAlias;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段别名
     */
    private String fieldAlias;
}
