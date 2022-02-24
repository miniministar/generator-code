
package com.exercise.generator.generatormulti.po;

import com.exercise.generator.generatormulti.config.StrategyConfig;
import com.exercise.generator.generatormulti.config.rules.NamingStrategy;
import com.exercise.generator.generatormulti.util.GeneratorUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 表信息，关联到当前字段信息
 */
@Data
@Accessors(chain = true)
public class TableInfo {

    private final Set<String> importPackages = new HashSet<>();
    private boolean convert;
    private String name;
    private String comment;
    private String entityName;
    private String mapperName;
    private String xmlName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;
    private String controllerReqName;
    private String controllerResName;
    private List<TableField> fields;
    /**
     * 公共字段
     */
    private List<TableField> commonFields;
    private String fieldNames;
    /**
     * 是否分页查询
     */
    private boolean pageFlag = true;
    /**
     * HTTP请求方法
     */
    private String requestMethod = "POST";
    /**
     * 请求地址
     */
    private String requestUrl;
    /**
     * 版本号
     */
    private String version;

    public TableInfo setConvert(boolean convert) {
        this.convert = convert;
        return this;
    }

    protected TableInfo setConvert(StrategyConfig strategyConfig) {
        if (strategyConfig.containsTablePrefix(name)) {
            // 包含前缀
            this.convert = true;
        } else if (strategyConfig.isCapitalModeNaming(name)) {
            // 包含
            this.convert = false;
        } else {
            // 转换字段
            if (NamingStrategy.underline_to_camel == strategyConfig.getColumnNaming()) {
                // 包含大写处理
                if (GeneratorUtils.containsUpperCase(name)) {
                    this.convert = true;
                }
            } else if (!entityName.equalsIgnoreCase(name)) {
                this.convert = true;
            }
        }
        return this;
    }


    public String getEntityPath() {
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
    }

    public TableInfo setEntityName(StrategyConfig strategyConfig, String entityName) {
        this.entityName = entityName;
        this.setConvert(strategyConfig);
        return this;
    }

    public TableInfo setFields(List<TableField> fields) {
        if (fields != null && fields.size() > 0) {
            this.fields = fields;
            // 收集导入包信息
            for (TableField field : fields) {
                if (null != field.getColumnType() && null != field.getColumnType().getPkg()) {
                    importPackages.add(field.getColumnType().getPkg());
                }
//                if (field.isKeyFlag()) {
//                    // 主键
//                    if (field.isConvert() || field.isKeyIdentityFlag()) {
//                        importPackages.add(com.baomidou.mybatisplus.annotation.TableId.class.getCanonicalName());
//                    }
//                    // 自增
//                    if (field.isKeyIdentityFlag()) {
//                        importPackages.add(com.baomidou.mybatisplus.annotation.IdType.class.getCanonicalName());
//                    }
//                } else if (field.isConvert()) {
//                    // 普通字段
//                    importPackages.add(com.baomidou.mybatisplus.annotation.TableField.class.getCanonicalName());
//                }
//                if (null != field.getFill()) {
//                    // 填充字段
//                    importPackages.add(com.baomidou.mybatisplus.annotation.TableField.class.getCanonicalName());
//                    importPackages.add(com.baomidou.mybatisplus.annotation.FieldFill.class.getCanonicalName());
//                }
            }
        }
        return this;
    }

    public TableInfo setImportPackages(String pkg) {
        importPackages.add(pkg);
        return this;
    }

//    /**
//     * 逻辑删除
//     */
//    public boolean isLogicDelete(String logicDeletePropertyName) {
//        return fields.parallelStream().anyMatch(tf -> tf.getName().equals(logicDeletePropertyName));
//    }
//
//    /**
//     * 转换filed实体为 xml mapper 中的 base column 字符串信息
//     */
//    public String getFieldNames() {
//        if (StringUtils.isEmpty(fieldNames)) {
//            StringBuilder names = new StringBuilder();
//            IntStream.range(0, fields.size()).forEach(i -> {
//                TableField fd = fields.get(i);
//                if (i == fields.size() - 1) {
//                    names.append(fd.getName());
//                } else {
//                    names.append(fd.getName()).append(", ");
//                }
//            });
//            fieldNames = names.toString();
//        }
//        return fieldNames;
//    }
}
