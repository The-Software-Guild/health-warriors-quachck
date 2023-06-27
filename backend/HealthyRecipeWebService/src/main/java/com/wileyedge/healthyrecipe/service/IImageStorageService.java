package com.wileyedge.healthyrecipe.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IImageStorageService {

	String uploadImage(MultipartFile imageFile) throws IOException;

}
