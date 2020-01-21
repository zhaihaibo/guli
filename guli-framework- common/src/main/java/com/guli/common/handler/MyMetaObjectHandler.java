package com.guli.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author helen
 * @since 2019/8/1
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		log.info("start insert fill.....");

		//注意：
		// 第一个参数是属性名
		// 第二个参数要和实体类中的属性类型保持一致
		this.setFieldValByName("gmtCreate", new Date(), metaObject);//版本号3.0.6以及之前的版本
		this.setFieldValByName("gmtModified", new Date(), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.info("start update fill.....");
		this.setFieldValByName("gmtModified", new Date(), metaObject);
	}
}
