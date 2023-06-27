package com.wileyedge.healthyrecipe.service;


import org.springframework.web.multipart.MultipartFile;

public class ImageStorageServiceMock implements IImageStorageService {

	@Override
	public String uploadImage(MultipartFile image) {
		return "dummy_s3_key";
	}
	
}
