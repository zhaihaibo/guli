package com.guli.common.exception;

import com.guli.common.constants.ResultCodeEnum;
import lombok.Data;

//处理运行时异常的自定义异常类
@Data
public class GuliException extends  RuntimeException {

    private  Integer code;

    public GuliException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public GuliException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code=resultCodeEnum.getCode();

    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message="+this.getMessage()+
                '}';
    }
}
