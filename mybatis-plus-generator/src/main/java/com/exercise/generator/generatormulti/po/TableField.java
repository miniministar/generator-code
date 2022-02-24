
package com.exercise.generator.generatormulti.po;

import com.exercise.generator.generatormulti.config.StrategyConfig;
import com.exercise.generator.generatormulti.config.rules.IColumnType;
import com.exercise.generator.generatormulti.config.rules.NamingStrategy;
import lombok.Data;

import java.util.Map;

/**
 * 表字段信息
 */
@Data
public class TableField implements Cloneable {

    private boolean convert;
    private boolean keyFlag;
    /**
     * 是否为查询条件
     */
    private boolean queryFlag;
    /**
     * 主键是否为自增类型
     */
    private boolean keyIdentityFlag;
    private String name;
    /**
     * 表别名
     */
    private String tableAlias;
    /**
     * 字段别名
     */
    private String nameAlias;
    private String type;
    private String propertyName;
    private IColumnType columnType;
    private String comment;
    private String fill;
    /**
     * 自定义查询字段列表
     */
    private Map<String, Object> customMap;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public TableField setConvert(boolean convert) {
        this.convert = convert;
        return this;
    }

    protected TableField setConvert(StrategyConfig strategyConfig) {
        if (strategyConfig.isEntityTableFieldAnnotationEnable()) {
            this.convert = true;
            return this;
        }
        if (strategyConfig.isCapitalModeNaming(name)) {
            this.convert = false;
        } else {
            // 转换字段
            if (NamingStrategy.underline_to_camel == strategyConfig.getColumnNaming()) {
                // 包含大写处理
                if (this.containsUpperCase(name)) {
                    this.convert = true;
                }
            } else if (!name.equals(propertyName)) {
                this.convert = true;
            }
        }
        return this;
    }

    /**
     * 包含大写字母
     *
     * @param word 待判断字符串
     * @return ignore
     */
    private boolean containsUpperCase(String word) {
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    public TableField setPropertyName(StrategyConfig strategyConfig, String propertyName) {
        this.propertyName = propertyName;
        this.setConvert(strategyConfig);
        return this;
    }

    public String getPropertyType() {
        if (null != columnType) {
            return columnType.getType();
        }
        return null;
    }
//
//    /**
//     * 按JavaBean规则来生成get和set方法
//     */
//    public String getCapitalName() {
//        if (propertyName.length() <= 1) {
//            return propertyName.toUpperCase();
//        }
//        String setGetName = propertyName;
//        if (DbColumnType.BASE_BOOLEAN.getType().equalsIgnoreCase(columnType.getType())) {
//            setGetName = StringUtils.removeIsPrefixIfBoolean(setGetName, Boolean.class);
//        }
//        // 第一个字母 小写、 第二个字母 大写 ，特殊处理
//        String firstChar = setGetName.substring(0, 1);
//        if (Character.isLowerCase(firstChar.toCharArray()[0])
//            && Character.isUpperCase(setGetName.substring(1, 2).toCharArray()[0])) {
//            return firstChar.toLowerCase() + setGetName.substring(1);
//        }
//        return firstChar.toUpperCase() + setGetName.substring(1);
//    }
}
