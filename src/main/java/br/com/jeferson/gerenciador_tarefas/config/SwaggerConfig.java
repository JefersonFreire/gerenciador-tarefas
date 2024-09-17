package br.com.jeferson.gerenciador_tarefas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    String schemeNome = "bearerAuth";
    String bearerFormat = "Jwt";
    String scheme = "bearer";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                        .addSecurityItem(new SecurityRequirement()
                                .addList(schemeNome)).components(new Components()
                                .addSecuritySchemes(
                                        schemeNome, new SecurityScheme()
                                                .name(schemeNome)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme(scheme)
                                                .bearerFormat(bearerFormat)
                                                .in(SecurityScheme.In.HEADER)

                                ))
                .info(new Info()
                        .version("0.0.1")
                        .title("API Gerenciamento de Tarefas")
                        .description("Documentação da API de Gerenciamento de Tarefas")
                );

    }
}
