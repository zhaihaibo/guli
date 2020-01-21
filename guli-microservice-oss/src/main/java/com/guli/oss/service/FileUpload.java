package com.guli.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileUpload {
    String upload(MultipartFile multipartFile,String host) throws IOException;
}
