package gourmetflow_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gourmetFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GourmetFlow Enterprise API")
                        .description("API REST backend de nivel empresarial para la gestión integral de restaurantes. " +
                                "Construida con Java 21, Spring Boot, seguridad JWE avanzada, cifrado AES en base de datos " +
                                "y arquitectura limpia orientada a principios SOLID.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Bryan Alexis Valencia Albornoz")
                                .email("dev.gourmetflow@enterprise.com")
                                .url("https://github.com/albornnoke"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}