package com.dreamshops.security.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Value("${springdoc.server.url}")
    private String serverUrl;

    @Value("${springdoc.server.description}")
    private String serverDescription;

   @Bean
   public OpenAPI defineOpenApi() {
       Server server = new Server();
       server.setUrl(serverUrl);
       server.setDescription(serverDescription);

       Contact myContact = new Contact();
       myContact.setName("Amit Vishwakarma");
       myContact.setEmail("vishwakarma.amit@outlook.in");

       Info information = new Info()
               .title("Dream shop API")
               .version("1.0")
               .description("This API exposes endpoints to manage dream shop ecommerce application.")
               .contact(myContact);
       return new OpenAPI().info(information).servers(List.of(server));
   }
}