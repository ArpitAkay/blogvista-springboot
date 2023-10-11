package com.blogvista.backend.util;

import com.amazonaws.services.s3.AmazonS3;
import com.blogvista.backend.exception.RESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class S3Util {
    private static final Logger logger = LoggerFactory.getLogger(S3Util.class);
    private final AmazonS3 amazonS3;
    @Value("${application.bucket.name}")
    private String bucketName;

    public S3Util(
            AmazonS3 amazonS3
    ) {
        logger.info("S3Util constructor called");
        this.amazonS3 = amazonS3;
    }

    public String uploadFileToS3Bucket(
            MultipartFile multipartFile
    ) throws RESTException {
        File file = convertMultipartFileToFile(multipartFile);
        String fileNameWithUUID = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        amazonS3.putObject(bucketName, fileNameWithUUID, file);
        file.delete();
        return fileNameWithUUID;
    }

    public String getImageUrlFromS3(String fileName) {
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public byte[] downloadFileFromS3Bucket(
            String fileName
    ) throws IOException {
        return amazonS3.getObject(bucketName, fileName).getObjectContent().readAllBytes();
    }

    public String deleteFileInS3Bucket(
            String fileName
    ) {
        amazonS3.deleteObject(bucketName, fileName);
        return "File deleted : " + fileName;
    }

    private File convertMultipartFileToFile(
            MultipartFile multipartFile
    ) throws RESTException {
        File convertedFile = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
            fileOutputStream.write(multipartFile.getBytes());
        } catch (Exception e) {
            throw new RESTException(e.getMessage());
        }
        return convertedFile;
    }
}
