package com.exercise.generator.generatormulti.config.converts;

import com.exercise.generator.generatormulti.config.ITypeConvert;
import com.exercise.generator.generatormulti.config.rules.DbColumnType;
import com.exercise.generator.generatormulti.config.rules.IColumnType;

public class MySqlTypeConvert implements ITypeConvert {

    public IColumnType processTypeConvert(String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return DbColumnType.STRING;
        } else if (t.contains("bigint")) {
            return DbColumnType.LONG;
        } else if (t.contains("tinyint(1)")) {
            return DbColumnType.BOOLEAN;
        } else if (t.contains("int")) {
            return DbColumnType.INTEGER;
        } else if (t.contains("text")) {
            return DbColumnType.STRING;
        } else if (t.contains("bit")) {
            return DbColumnType.BOOLEAN;
        } else if (t.contains("decimal")) {
            return DbColumnType.BIG_DECIMAL;
        } else if (t.contains("clob")) {
            return DbColumnType.CLOB;
        } else if (t.contains("blob")) {
            return DbColumnType.BLOB;
        } else if (t.contains("binary")) {
            return DbColumnType.BYTE_ARRAY;
        } else if (t.contains("float")) {
            return DbColumnType.FLOAT;
        } else if (t.contains("double")) {
            return DbColumnType.DOUBLE;
        } else if (t.contains("json") || t.contains("enum")) {
            return DbColumnType.STRING;
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            if ("date".equals(t)) {
                return DbColumnType.LOCAL_DATE;
            } else if ("time".equals(t)) {
                return DbColumnType.LOCAL_TIME;
            } else if ("year".equals(t)) {
                return DbColumnType.YEAR;
            }
            return DbColumnType.LOCAL_DATE_TIME;
        }
        return DbColumnType.STRING;
    }
}
