package com.challenger.fridge.swagger;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import static io.swagger.v3.oas.models.security.SecurityScheme.*;

@Configuration
public class OpenApiConfig {
    private static final String SECURITY_SCHEME_NAME="authorization";
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("냉글벙글 API")
                .version("1.0")
                .description("API명세 입니다.");
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .info(info);
    }
}
