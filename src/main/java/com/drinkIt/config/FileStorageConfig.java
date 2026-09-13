package com.drinkIt.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileStorageConfig
        implements WebMvcConfigurer {

    @Value("${app.upload.directory:uploads/products}")
    private String uploadDirectory;


    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        Path uploadPath =
                Paths.get(
                        uploadDirectory
                )
                .toAbsolutePath()
                .normalize();


        registry.addResourceHandler(
                "/uploads/products/**"
        )
        .addResourceLocations(
                uploadPath.toUri().toString()
        );
    }
}