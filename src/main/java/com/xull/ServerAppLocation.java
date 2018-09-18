package com.xull;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
public class ServerAppLocation  {
    public static void main(String[] args) {
        System.setProperty("org.freemarker.loggerLibrary", "SLF4J");
        SpringApplication.run(ServerAppLocation.class, args);
    }
}
