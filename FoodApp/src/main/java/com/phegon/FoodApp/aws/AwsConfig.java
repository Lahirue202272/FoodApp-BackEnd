package com.phegon.FoodApp.aws;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration//This class contains configuration/setup code.So this is not business logic like controller or service.
public class AwsConfig {

    @Value("${aws.s3.region}")
    private String awsRegion;

    @Value("${aws.accessKeyId}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Bean
    public StaticCredentialsProvider staticCredentialsProvider(){
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKey, awsSecretKey));
    }

    @Bean//Because Spring will create this object once and manage it.
    public S3Client s3Client(StaticCredentialsProvider credentialsProvider) {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(credentialsProvider)
                .build(); //This method creates the main AWS S3 client object.This is the object your app will use to actually talk to S3.

    }
}

//What is AwsBasicCredentials?This combines:access key,secret key.So this is your AWS login identity.

//What is StaticCredentialsProvider?This gives those credentials to AWS SDK.It is called “static” because the credentials are fixed values from config.
//Simple meaning:This is the object that holds your AWS login details ready for use.

//What is S3Client?This is the AWS SDK client for S3.It is the object used to do things like:upload file,download file,delete file,list files
//Very simple meaning:This is the actual tool that talks to AWS S3.

//Step 1:It reads values from application.properties:aws.s3.region,aws.accessKeyId,aws.secretKey
//Step 2:It creates StaticCredentialsProvider
//Step 3:It creates S3Client
//Step 4:Other classes can now use S3Client