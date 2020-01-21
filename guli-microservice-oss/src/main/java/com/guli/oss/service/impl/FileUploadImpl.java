package com.guli.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.guli.oss.service.FileUpload;
import com.guli.oss.util.OssUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileUploadImpl implements FileUpload {
    @Override
    public String upload(MultipartFile multipartFile, String host) throws IOException {

        //桶的名字
        String bucknetName = OssUtils.BUCKET_NAME;
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = OssUtils.END_POINT;

// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId =OssUtils.KEY_ID;
        String accessKeySecret = OssUtils.KEY_SECRET;

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //健壮性考虑，没桶先创建
        if (!ossClient.doesBucketExist(bucknetName)){
            //创建bucket
            ossClient.createBucket(bucknetName);
            //设置oss实例的访问权限：公共读
            ossClient.setBucketAcl(bucknetName, CannedAccessControlList.PublicRead);
        }
// 上传文件流。

        InputStream inputStream = multipartFile.getInputStream();

        //构建日期路径：avatar/2019/02/26/文件名
        String filePath = new DateTime().toString("yyyy/MM/dd");

        //文件名：uuid.扩展名
        String original = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();
        String fileType = original.substring(original.lastIndexOf("."));
        String newName = fileName + fileType;
        String fileUrl = host + "/" + filePath + "/" + newName;

        ossClient.putObject(bucknetName, fileUrl, inputStream);

// 关闭OSSClient。
        ossClient.shutdown();

        //返回一个字符串存入数据库

        //获取url地址
        String returnUrl = "http://" + bucknetName + "." + endpoint + "/" + fileUrl;
        return returnUrl;
    }
}
