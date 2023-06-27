package com.wileyedge.healthyrecipe.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Component
public class ImageStorageService implements IImageStorageService {

    private final S3Client s3Client;
    private final String bucketName;

    public ImageStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
        this.bucketName = "foodimagesbucket";
    }

    @Override
    public String uploadImage(MultipartFile imageFile) throws IOException {
        System.out.println("ImageFileName : " + imageFile.getOriginalFilename());
        String fileName = imageFile.getOriginalFilename();
        File file = convertMultiPartFileToFile(imageFile);
        String s3Key = fileName + "_" + System.currentTimeMillis() + "_" + fileName;

        System.out.println("S3 after generated append with time");
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        s3Client.putObject(request, file.toPath());

        file.delete();

        System.out.println("s3 key returning from AWS by upload image : " + s3Key);
        return s3Key;
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }
}
