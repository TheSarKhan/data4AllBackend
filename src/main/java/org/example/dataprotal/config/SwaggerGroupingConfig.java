package org.example.dataprotal.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerGroupingConfig {

    // Group 1: Integration – Analytics API
    @Bean
    public GroupedOpenApi integrationAnalyticApi() {
        return GroupedOpenApi.builder()
                .group("Integration – Analytic API")
                .pathsToMatch("/api/v1/analytic/**")
                .addOpenApiCustomizer(titleCustomizer("Integration Analytic API"))
                .packagesToScan("org.example.dataprotal.controller.analytic")
                .build();
    }

    // Group 2: Core application API
    @Bean
    public GroupedOpenApi coreApi() {
        return GroupedOpenApi.builder()
                .group("Core API")
                .pathsToMatch("/api/v1/**")
                .pathsToExclude("/api/v1/analytic/**")
                .packagesToScan("org.example.dataprotal.controller")
                .build();
    }

    private OpenApiCustomizer titleCustomizer(String title) {
        return openApi -> openApi.setInfo(new Info().title(title).version("v1"));
    }
}
