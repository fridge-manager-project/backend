package com.challenger.fridge.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("냉글벙글 API")
                .version("1.0")
                .description("API명세 입니다.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}