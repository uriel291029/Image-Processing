package com.dojo.challenge.imageprocessingservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Image Processing API", version = "1.0", description = "Image Processing Service"))
public class ImageProcessingServiceApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ImageProcessingServiceApplication.class)
                .profiles("prod")
                .run(args);
    }
}
