package com.restgram.global.s3.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadFile(MultipartFile multipartFile, String url);
    boolean delete(String fileUrl);
}
