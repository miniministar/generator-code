package com.exercise.generator.generatormulti.config.querys;

import com.exercise.generator.generatormulti.config.IDbQuery;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDbQuery implements IDbQuery {


    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return false;
    }


    public String[] fieldCustom() {
        return null;
    }
}
