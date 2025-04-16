package org.motion.buscar_api.application.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    // OpenAPI configuration
    // Comentar esse metodo para uso local do swagger.
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("https://buscar.motionweb.me/api"))
                .info(new Info().title("Buscar API").version("v1"));
    }
}
