package com.movie_event_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Movie Service API")
                        .description("API for managing movies ,shows, and genres in BookMyShow clone application")
                        .version("1.0.0")
                        .contact(
                                new Contact()
                                        .name("BookMyShow Team")
                                        .email("support@bookmyshow.com")
                                        .url("https://bookmyshow.com"))
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
