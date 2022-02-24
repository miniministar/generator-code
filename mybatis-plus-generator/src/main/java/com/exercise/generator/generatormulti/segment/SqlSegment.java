package com.exercise.generator.generatormulti.segment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlSegment {


    /**
     * Sql语句片段开头部分
     */
    protected String start;
    /**
     * Sql语句片段中间部分
     */
    protected String body;
    /**
     * Sql语句片段结束部分
     */
    protected String end;
    /**
     * 用于分割中间部分的正则表达式
     */
    protected String bodySplitPattern;
    /**
     * 表示片段的正则表达式
     */
    protected String segmentRegExp;
    /**
     * 分割后的Body小片段
     */
    protected List<String> bodyPieces;

    /**
     * 构造函数
     *
     * @param segmentRegExp    表示这个Sql片段的正则表达式
     * @param bodySplitPattern 用于分割body的正则表达式
     */
    public SqlSegment(String segmentRegExp, String bodySplitPattern) {
        start = "";
        body = "";
        end = "";
        this.segmentRegExp = segmentRegExp;
        this.bodySplitPattern = bodySplitPattern;
        this.bodyPieces = new ArrayList<>();

    }

    /**
     * 从sql中查找符合segmentRegExp的部分，并赋值到start,body,end等三个属性中
     *
     * @param sql 待解析的sql
     */
    public void parse(String sql) {
        Pattern pattern = Pattern.compile(segmentRegExp, Pattern.CASE_INSENSITIVE);
        for (int i = 0; i <= sql.length(); i++) {
            String shortSql = sql.substring(0, i);
//            logger.info(shortSql);
            Matcher matcher = pattern.matcher(shortSql);
            while (matcher.find()) {
//                logger.info("count：" + matcher.groupCount());
                start = matcher.group(1);
//                logger.info("start：" + start);
                body = matcher.group(2);
//                logger.info("body：" + body);
                end = matcher.group(3);
//                logger.info("end：" + end);
                parseBody();
                return;
            }
        }
    }

    /**
     * 解析body部分
     */
    protected void parseBody() {
//        List<String> ls = new ArrayList<String>();
//        Pattern p = Pattern.compile(bodySplitPattern, Pattern.CASE_INSENSITIVE);
//        // 先清除掉前后空格
//        body = body.trim();
//        Matcher m = p.matcher(body);
//        StringBuffer sb = new StringBuffer();
//        boolean result = m.find();
//        while (result) {
//            m.appendReplacement(sb, m.group(0) + Crlf);
//            result = m.find();
//        }
//        m.appendTail(sb);
//        // 再按空格断行
//        String[] arr = sb.toString().split(" ");
//        int arrLength = arr.length;
//        for (int i = 0; i < arrLength; i++) {
//            String temp = FourSpace + arr[i];
//            if (i != arrLength - 1) {
//                //temp=temp+Crlf;
//            }
//            logger.info("bodytemp:" + temp);
//            ls.add(temp);
//        }
//        bodyPieces = ls;
    }

    /**
     * sql预处理
     *
     * @param sql
     * @return
     */
    protected String pretreatmentSql(String sql) {
        return sql;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
