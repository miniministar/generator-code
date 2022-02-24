package com.exercise.generator.generatormulti.segment;

/**
 * 查询条件sql片段
 * Created by lige  2017/12/18.
 * <a href="ge.li@wake8.com" />
 */
public class WhereSqlSegment extends SqlSegment {

    public WhereSqlSegment(String segmentRegExp, String bodySplitPattern) {
        super(segmentRegExp, bodySplitPattern);
    }

    @Override
    protected void parseBody() {
        body = body.trim();
        System.out.println("查询sql语句条件不需要解析,原封不动的保存");
    }

    @Override
    protected String pretreatmentSql(String sql) {
        if (!sql.contains("where")) {
            System.out.println("sql不包含条件部分");
            return sql;
        }
        return sql.substring(sql.indexOf("where"));
    }
}
