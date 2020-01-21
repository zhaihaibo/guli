package com.guli.oss.controller;

import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.R;
import com.guli.oss.service.FileUpload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(description = "阿里云文件管理")
@RestController
@CrossOrigin
@RequestMapping("/admin/oss/file")
public class FileUploadController {

    @Autowired
    FileUpload fileUpload;

    @ApiOperation("上传文件")
    @PostMapping
    public R uplaod(
            @ApiParam(name = "file",value = "文件")
            @RequestParam("file") MultipartFile file,
            @RequestParam("host") String host)  {


        String uploadUrl = null;
        try {
            uploadUrl = fileUpload.upload(file, host);
            return R.ok().message("文件上传成功").data("url", uploadUrl);
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }


    }

}
