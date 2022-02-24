package com.exercise.generator.generatormulti.segment;

import com.exercise.generator.generatormulti.po.TablePart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表sql语句片段
 * Created by lige  2017/12/18.
 * <a href="ge.li@wake8.com" />
 */
public class TableSqlSegment extends SqlSegment {

    /**
     * 外连表解析
     */
    private static final String JOIN_TABLE_REGEXP = "([a-z_0-9]+)\\s([a-z_0-9]+)";
    /**
     * sql表封装类集合
     */
    private List<TablePart> tablePartList = new ArrayList<>();

    public TableSqlSegment(String segmentRegExp, String bodySplitPattern) {
        super(segmentRegExp, bodySplitPattern);
    }

    @Override
    protected void parseBody() {
        body = body.trim();
        List<String> tableSqlList = new ArrayList<>();
        if (body.contains("left join") || body.contains("right join") || body.contains("inner join")) {
            tableSqlList = Arrays.asList(body.split("join"));

        } else if (body.contains(",")) {
            tableSqlList = Arrays.asList(body.split(","));

        } else {
            tableSqlList.add(body);
//            System.out.println("解析表模块错误，body：" + body);
        }
        Pattern joinTableRegExp = Pattern.compile(JOIN_TABLE_REGEXP, Pattern.CASE_INSENSITIVE);
        for (String tableSql : tableSqlList) {
//            logger.info(tableSql);
            Matcher matcher = joinTableRegExp.matcher(tableSql);
            if (matcher.find()) {
//                logger.info(matcher.group(1) + "," + matcher.group(2));
                tablePartList.add(this.setTableInfoX(matcher.group(1), matcher.group(2)));
            } else {
                System.out.println("解析表模块错误，body：" + body);
            }
        }
    }

    /**
     * 设置表信息
     *
     * @param tableName  表明
     * @param tableAlias 表别名
     * @return
     */
    private TablePart setTableInfoX(String tableName, String tableAlias) {
        TablePart tablePart = new TablePart();
        tablePart.setTableAlias(tableAlias);
        tablePart.setTableName(tableName);
        return tablePart;
    }

    public List<TablePart> getTablePartList() {
        return tablePartList;
    }

    public void setTablePartList(List<TablePart> tablePartList) {
        this.tablePartList = tablePartList;
    }
}
