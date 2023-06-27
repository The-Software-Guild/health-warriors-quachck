package com.wileyedge.healthyrecipe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
public class AppConfig {
	
	@Autowired
	private Environment environment;

	@Bean
	public S3Client generateS3Client() {
		String accessKey = environment.getProperty("aws.accessKey");
		String accessSecret = environment.getProperty("aws.secretKey");
		
		AwsCredentials credentials = AwsBasicCredentials.create(accessKey, accessSecret);
		return S3Client.builder()
				.region(Region.AP_SOUTHEAST_2)
				.credentialsProvider(StaticCredentialsProvider.create(credentials))
				.build();
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(10485760); // Set the maximum file size allowed (in bytes)
		return resolver;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
