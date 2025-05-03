package com.dev.br.campeonato_brasileiro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Campeonato Brasileiro")
                        .description("Documentação da API para gerenciamento do Campeonato Brasileiro Série A")
                        .version("1.0"));
    }
}
