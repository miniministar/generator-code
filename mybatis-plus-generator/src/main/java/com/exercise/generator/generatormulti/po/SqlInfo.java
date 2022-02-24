package com.exercise.generator.generatormulti.po;

import lombok.Data;

/**
 * 查询方法封装类
 * Created by lige  2017/12/22.
 * <a href="ge.li@wake8.com" />
 */
@Data
public class SqlInfo {
    /**
     * 查询sql
     */
    private String querySQL;
    /**
     * 表sql
     */
    private String tableSQL;
    /**
     * 查询条件sql
     */
    private String whereSQL;
    /**
     * 分组sql
     */
    private String groupBySQL;
    /**
     * 排序sql
     */
    private String orderBySQL;
}
