package com.exercise.generator.generatormulti.segment;

/**
 * sql排序语句片段
 * Created by lige  2017/12/19.
 * <a href="ge.li@wake8.com" />
 */
public class OrderBySqlSegment extends SqlSegment {

    public OrderBySqlSegment(String segmentRegExp, String bodySplitPattern) {
        super(segmentRegExp, bodySplitPattern);
    }

    @Override
    protected void parseBody() {
        body = body.trim();
        System.out.println("sql排序条件不需要解析,原封不动的保存");
    }

    @Override
    protected String pretreatmentSql(String sql) {
        if (!sql.contains("order")) {
            System.out.println("sql不包含排序内容");
            return sql;
        }
        return sql.substring(sql.indexOf("order"));
    }
}
