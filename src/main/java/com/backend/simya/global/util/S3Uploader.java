package com.backend.simya.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.backend.simya.global.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.backend.simya.global.common.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile image) throws BaseException {
        String fileName = createFileName(image.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(image.getSize());
        objectMetadata.setContentType(image.getContentType());

        try(InputStream inputStream = image.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new BaseException(FAILED_TO_UPLOAD_IMAGE);
        }
       return fileName;
    }

    public void deleteImage(String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String createFileName(String fileName) throws BaseException {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) throws BaseException {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new BaseException(INVALID_FILE_TYPE);
        }
    }
}
