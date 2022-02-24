package com.exercise.generator.generatormulti.config.querys;

import com.exercise.generator.generatormulti.annotation.DbType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MySql 表数据查询
 *
 * @author hubin
 * @since 2018-01-16
 */
public class MySqlQuery extends AbstractDbQuery {


    public DbType dbType() {
        return DbType.MYSQL;
    }


    public String tablesSql() {
        return "show table status";
    }


    public String tableFieldsSql() {
        return "show full fields from `%s`";
    }


    public String tableName() {
        return "NAME";
    }


    public String tableComment() {
        return "COMMENT";
    }


    public String fieldName() {
        return "FIELD";
    }


    public String fieldType() {
        return "TYPE";
    }


    public String fieldComment() {
        return "COMMENT";
    }


    public String fieldKey() {
        return "KEY";
    }


    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return "auto_increment".equals(results.getString("Extra"));
    }
}
