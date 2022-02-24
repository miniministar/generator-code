package com.exercise.generator.generatormulti.po;

import lombok.Data;

/**
 * @description:
 * @author: ZL
 * @date: 2019-06-19 14:44
 */
@Data
public class TablePart {

    /**
     * 表别名
     */
    private String tableAlias;
    /**
     * 表名
     */
    private String tableName;
}
