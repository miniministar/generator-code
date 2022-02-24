package com.exercise.generator.generatormulti.segment;

/**
 * sql分组语句片段
 * Created by lige  2017/12/19.
 * <a href="ge.li@wake8.com" />
 */
public class GroupBySqlSegment extends SqlSegment {


    public GroupBySqlSegment(String segmentRegExp, String bodySplitPattern) {
        super(segmentRegExp, bodySplitPattern);
    }

    @Override
    protected void parseBody() {
        body = body.trim();
        System.out.println("sql分组条件不需要解析,原封不动的保存");
    }

    @Override
    protected String pretreatmentSql(String sql) {
        if (!sql.contains("group")) {
            System.out.println("sql不包含分组部分");
            return sql;
        }
        return sql.substring(sql.indexOf("group"));
    }
}
