package com.exercise.generator.generatormulti;

import com.exercise.generator.generatormulti.config.*;
import com.exercise.generator.generatormulti.config.rules.NamingStrategy;

/**
 * @description:
 * @author: ZL
 * @date: 2019-06-21 12:05
 */
public class CodeGeneratorBySQL {

    public static void main(String[] args) {
        String localPath = "/pms-generator";
        String projectPath = System.getProperty("user.dir") + localPath;
        String basePack = "cn.com.hopson.pms.demo";
//        String projectName = "tenant";//platform system tenant
//        String localPath = "/pms-" + projectName + "-server";
//        String projectPath = System.getProperty("user.dir") + localPath;
//        String basePack = "cn.com.hopson.pms." + projectName;
        AutoGeneratorBySQL mpgBySQL = new AutoGeneratorBySQL();
        String sql = "SELECT " +
                "id, " +
                "username, " +
                "nickname, " +
                "gender, " +
                "password, " +
                "dept_id, " +
                "avatar, " +
                "mobile, " +
                "status, " +
                "email, " +
                "deleted, " +
                "gmt_create, " +
                "gmt_modified, " +
                "employee_id, " +
                "postition_code, " +
                "key_post_code, " +
                "leader_employee_id, " +
                "first_name, " +
                "last_name, " +
                "birthday, " +
                "phone_number, " +
                "office_phone, " +
                "first_level_dept, " +
                "second_level_dept, " +
                "third_level_dept, " +
                "fourth_level_dept, " +
                "fifth_level_dept, " +
                "sixth_level_dept, " +
                "id_card, " +
                "resignation_date, " +
                "level_code, " +
                "nationality, " +
                "rank, " +
                "cadre, " +
                "office_email, " +
                "entry_date, " +
                "last_update_date, " +
                "employee_type, " +
                "passport_no, " +
                "post_code, " +
                "english_name, " +
                "org_path, " +
                "res_location, " +
                "sub_posison, " +
                "emp_rel, " +
                "education, " +
                "institution, " +
                "major, " +
                "full_name, " +
                "disable_time, " +
                "tax_org_code  " +
                "FROM " +
                "sys_user";
        MainConfig mainConfig = new MainConfig();
        mainConfig.setSql(sql);
        mainConfig.setBaseName("tAppMenuList");
        mainConfig.setPageFlag(true);
        mainConfig.setDescription("平台应用列表");
        mainConfig.setVersion("v1.0.0");
        mainConfig.setParams(new String[]{"t_user_id"});
        mpgBySQL.setMainConfig(mainConfig);
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://10.255.10.146:3306/sany-ren-ant-platform?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpgBySQL.setDataSourceConfig(dsc);
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setOutputXMLDir(projectPath + "/src/main/resources");
        gc.setAuthor("hopson");
        gc.setFileOverride(false);// 是否覆盖同名文件，默认是false
        gc.setSwagger2(true);
        gc.setOpen(false);
        mpgBySQL.setGlobalConfig(gc);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        mpgBySQL.setStrategyConfig(strategy);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(basePack);//父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
        mpgBySQL.setPackageConfig(pc);
        mpgBySQL.execute();
    }
}
