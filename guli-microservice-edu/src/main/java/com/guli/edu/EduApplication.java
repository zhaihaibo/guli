package com.guli.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author helen
 * @since 2019/8/2
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.guli.common","com.guli.edu"})
public class EduApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduApplication.class, args);
	}
}
