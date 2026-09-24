package htw.freiheit.user.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userApiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("DemoUser API")
                        .description("REST-API zur Registrierung und Anmeldung von Nutzerinnen und Nutzern. "
                                + "Teil des Lehrprojekts Softwareentwicklungsprojekt (HTW Berlin).")
                        .version("v0.1"));
    }
}
