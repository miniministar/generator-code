package com.exercise.generator.generatormulti.segment;

import com.exercise.generator.generatormulti.po.QueryPart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParamSqlSegment extends SqlSegment {


    private static final String QUERY_PARAM_REGEXP = "([a-z_0-9]+)\\.([a-z_0-9]+)\\s([a-z_0-9]+)";

    private static final String QUERY_PARAM_REGEXP_SHORTHAND = "([a-z_0-9]+)\\.([a-z_0-9]+)";
    /**
     * 查询参数封装类集合
     */
    private List<QueryPart> queryPartList = new ArrayList<>();

    public QueryParamSqlSegment(String segmentRegExp, String bodySplitPattern) {
        super(segmentRegExp, bodySplitPattern);
    }

    @Override
    protected void parseBody() {
        body = body.trim();
        List<String> queryParamList = Arrays.asList(body.split(bodySplitPattern));
        Pattern queryParamRegExp = Pattern.compile(QUERY_PARAM_REGEXP, Pattern.CASE_INSENSITIVE);
        Pattern shortHandRegExp = Pattern.compile(QUERY_PARAM_REGEXP_SHORTHAND, Pattern.CASE_INSENSITIVE);
        for (String queryParam : queryParamList) {
            queryParam = trim(queryParam);
            //解析sql参数
            Matcher matcher = queryParamRegExp.matcher(queryParam);
            if (matcher.matches()) {
//                logger.info(matcher.group(1) + ":" + matcher.group(2) + ":" + matcher.group(3));
                this.queryPartList.add(this.setTableFieldX(matcher.group(1), matcher.group(2), matcher.group(3)));
            } else {
                matcher = shortHandRegExp.matcher(queryParam);
                if (matcher.matches()) {
//                    logger.info(matcher.group(1) + ":" + matcher.group(2) + ":" + matcher.group(2));
                    this.queryPartList.add(this.setTableFieldX(matcher.group(1), matcher.group(2), matcher.group(2)));
                } else {
                    System.out.println("解析查询参数错误，查询字符串：" + queryParam);
                }
            }
        }
    }


    /**
     * 设置表字段信息
     *
     * @param tableAlias   表别名
     * @param name         字段名称
     * @param propertyName 字段别名
     * @return
     */
    private QueryPart setTableFieldX(String tableAlias, String name, String propertyName) {
        QueryPart queryPart = new QueryPart();
        queryPart.setTableAlias(tableAlias);
        queryPart.setFieldName(name);
        queryPart.setFieldAlias(propertyName);
        return queryPart;
    }


    /*左右空格都去掉*/
    public static String trim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            //return leftTrim(rightTrim(str));
            return str.replaceAll("^[　 ]+|[　 ]+$", "");
        }
    }

    /*去左空格*/
    public static String leftTrim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("^[　 ]+", "");
        }
    }

    /*去右空格*/
    public static String rightTrim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("[　 ]+$", "");
        }
    }

    public List<QueryPart> getQueryPartList() {
        return queryPartList;
    }

    public void setQueryPartList(List<QueryPart> queryPartList) {
        this.queryPartList = queryPartList;
    }
}
