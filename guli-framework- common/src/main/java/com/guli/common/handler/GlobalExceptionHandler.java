package com.guli.common.handler;

import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.util.ExceptionUtils;
import com.guli.common.vo.R;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author helen
 * @since 2019/8/2
 */
@ControllerAdvice
@Slf4j   //这个注解代表使用lomback记录日志！打印到硬盘
public class GlobalExceptionHandler {

	//捕获到exception异常
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public R error(Exception e){
		//e.printStackTrace();  打印栈信息，默认输出到控制台
		//log.error(e.getMessage());   打印到硬盘，但是不能打印栈信息
		log.error(ExceptionUtils.getMessage(e));  //打印到硬盘，工具类中封装了栈信息
 		return R.error();
	}

	//捕获到HttpMessageNotReadableException异常
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public R error(HttpMessageNotReadableException e){
	//	e.printStackTrace();
		//log.error(e.getMessage());   打印到硬盘，但是不能打印栈信息
		log.error(ExceptionUtils.getMessage(e));  //打印到硬盘，工具类中疯转了栈信息
		return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
	}

	//捕获到BadSqlGrammarException
	@ExceptionHandler(BadSqlGrammarException.class)
	@ResponseBody
	public R error(BadSqlGrammarException e){
		//e.printStackTrace();
		//log.error(e.getMessage());   打印到硬盘，但是不能打印栈信息
		log.error(ExceptionUtils.getMessage(e));  //打印到硬盘，工具类中疯转了栈信息
		return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
	}

	//捕获自定义runtime异常类
	@ExceptionHandler(GuliException.class)
	@ResponseBody
	public R error(GuliException e){
		//e.printStackTrace();
		//log.error(e.getMessage());   打印到硬盘，但是不能打印栈信息
		log.error(ExceptionUtils.getMessage(e));  //打印到硬盘，工具类中疯转了栈信息
		return R.error().code(e.getCode()).message(e.getMessage());

	}
}
