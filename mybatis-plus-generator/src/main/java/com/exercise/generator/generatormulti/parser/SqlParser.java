package com.exercise.generator.generatormulti.parser;


import com.exercise.generator.generatormulti.segment.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 联合查询sql解析
 * @author: ZL
 * @date: 2019-06-19 10:52
 */
public class SqlParser {

    /**
     * 原sql
     */
    private String origSql;
    /**
     * Sql语句片段
     */
    private List<SqlSegment> sqlSegments = new ArrayList<>();

    public SqlParser(String sql) {
        sql = this.initSql(sql);
        this.origSql = sql;
//        logger.info(sql);
        this.initSqlSegments();
        this.sqlParser();
    }

    private String initSql(String sql) {
        sql = sql.trim().toLowerCase();
        sql = sql.replaceAll("\\s{1,}", " ");
        sql = sql.replaceAll("\\n", "");
        sql = sql.replaceAll("\\r", "");
        sql = "" + sql + " ENDOFSQL";
        return sql;
    }

    private void initSqlSegments() {
        sqlSegments.add(new QueryParamSqlSegment("(select)(.+)(from)", "[,]"));
        sqlSegments.add(new TableSqlSegment("(from)(.+)(where|group by|order by|ENDOFSQL)", "(,| left join | right join | inner join )"));
        sqlSegments.add(new WhereSqlSegment("(where)(.+)(group by|order by|limit|ENDOFSQL)", "(and|or)"));
        sqlSegments.add(new GroupBySqlSegment("(group by)(.+)(order by|limit|ENDOFSQL)", "[,]"));
        sqlSegments.add(new OrderBySqlSegment("(order by)(.+)(limit|ENDOFSQL)", "[,]"));
    }

    private void sqlParser() {
        for (SqlSegment sqlSegment : sqlSegments) {
            sqlSegment.parse(origSql);
        }
    }

    public String getOrigSql() {
        return origSql;
    }

    public void setOrigSql(String origSql) {
        this.origSql = origSql;
    }

    public List<SqlSegment> getSqlSegments() {
        return sqlSegments;
    }

    public void setSqlSegments(List<SqlSegment> sqlSegments) {
        this.sqlSegments = sqlSegments;
    }
}
