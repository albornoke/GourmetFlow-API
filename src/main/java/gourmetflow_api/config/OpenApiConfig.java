package gourmetflow_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gourmetFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GourmetFlow API - Sistema de Gestión de Restaurante")
                        .description("API backend de alto rendimiento con seguridad de nivel bancario, cifrado AES, validación estricta y control de líneas base.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Bryan Albornoz - Ingeniería de Software")
                                .email("desarrollo@gourmetflow.com")));
    }
}