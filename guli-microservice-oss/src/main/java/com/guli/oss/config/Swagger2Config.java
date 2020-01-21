package com.guli.oss.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author helen
 * @since 2019/8/2
 */
@Configuration
@EnableSwagger2 //下文配置swaagger2
public class Swagger2Config {

	@Bean
	public Docket adminApiConfig(){
		return new Docket (DocumentationType.SWAGGER_2)
				.groupName("admin")   //groupname 是在swagger上分组，但是分完两组都显示所有
				.apiInfo(adminApiInfo())  //下边有具体实现
				.select()
				//只显示admin路径下的页面
				.paths(Predicates.and(PathSelectors.regex("/admin/.*")))
				.build();
	}


	private ApiInfo adminApiInfo(){  //admin 下的标题，描述之类的

		return new ApiInfoBuilder()
				.title("后台管理系统-阿里云文件管理")
				.description("本文档描述了后台管理系统阿里云文件管理微服务接口定义，你可以使用这个做测试")
				.version("1.0")
				.contact(new Contact("翟海波", "http://atguigu.com", "1149513559@qq.com"))
				.build();
	}
}

