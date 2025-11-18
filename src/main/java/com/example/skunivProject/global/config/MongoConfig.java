package com.example.skunivProject.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing  // @CreatedDate, @LastModifiedDate 활성화
public class MongoConfig {
}
