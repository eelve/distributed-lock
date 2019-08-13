package com.eelve.redissionlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Description Created by zeng.yubo on 2019/8/7.
 */
@SpringBootApplication
public class RedissonTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedissonTestApplication.class, args);
    }
}
