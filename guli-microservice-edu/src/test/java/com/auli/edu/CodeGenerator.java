package com.auli.edu;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author helen
 * @since 2019/8/2
 */
public class CodeGenerator {

	@Test
	public void genCode(){

		String moduleName = "edu";


		//1、创建代码生成器对象
		AutoGenerator mpg = new AutoGenerator();

		//2、全局配置
		GlobalConfig gc = new GlobalConfig();
		//user.dir代表的是当前操作的工程名字
		String projectPath = System.getProperty("user.dir");
		//设置代码的生成位置
		gc.setOutputDir(projectPath + "/src/main/java");
		gc.setAuthor("Helen");
		gc.setOpen(false); //生成后是否打开资源管理器
		gc.setFileOverride(false); //重新生成时文件是否覆盖
		gc.setServiceName("%sService");	//去掉Service接口的首字母I
		gc.setIdType(IdType.ID_WORKER_STR); //主键策略
		gc.setDateType(DateType.ONLY_DATE);//定义生成的实体类中日期类型
		gc.setSwagger2(true);//开启Swagger2模式
		mpg.setGlobalConfig(gc);

		// 3、数据源配置
		//spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
		//spring.datasource.url=jdbc:mysql://localhost:3306/guli_edu_190225?serverTimezone=GMT%2B8
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl("jdbc:mysql://localhost:3306/guli_" + moduleName+"_190225?serverTimezone=GMT%2B8");
		dsc.setDriverName("com.mysql.cj.jdbc.Driver");
		dsc.setUsername("root");
		dsc.setPassword("root");
		dsc.setDbType(DbType.MYSQL);
		mpg.setDataSource(dsc);

		// 4、包配置  可写可不写， 因为默认的就是这些
		PackageConfig pc = new PackageConfig();
		pc.setModuleName(moduleName); //模块名 edu
		pc.setParent("com.guli"); //==com.gulli.edu
		pc.setController("controller");
		pc.setEntity("entity");
		pc.setService("service");
		pc.setMapper("mapper");
		mpg.setPackageInfo(pc);

		// 5、策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setInclude(moduleName + "_\\w*");//设置要映射的表名 ,如果只针对一个表生成代码，可以写实这个表
		strategy.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略
		strategy.setTablePrefix(pc.getModuleName() + "_");//设置表前缀不生成

		strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段映射到实体的命名策略
		strategy.setEntityLombokModel(true); // lombok 模型 @Accessors(chain = true) setter链式操作

		strategy.setLogicDeleteFieldName("is_deleted");//逻辑删除字段名
		strategy.setEntityBooleanColumnRemoveIsPrefix(true);//去掉布尔值的is_前缀

		//自动填充
		TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
		TableFill gmtModified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
		ArrayList<TableFill> tableFills = new ArrayList<>();
		tableFills.add(gmtCreate);
		tableFills.add(gmtModified);
		strategy.setTableFillList(tableFills);

		strategy.setVersionFieldName("version");//乐观锁列

		strategy.setRestControllerStyle(true); //restful api风格控制器
		strategy.setControllerMappingHyphenStyle(true); //url中驼峰转连字符

		mpg.setStrategy(strategy);


		//6、运行代码生成器
		mpg.execute();
	}
}
