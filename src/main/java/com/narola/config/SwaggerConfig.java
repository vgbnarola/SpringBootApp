package com.narola.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
//@OpenAPIDefinition(info = @Info(title = "Spring Demo Application", description = "Demo Application for User management Service", version = "1.0.0"))
@SecurityScheme(
        name = "tokenapi",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "JWT",
        paramName = "Authorization"
)
public class SwaggerConfig {

    @Value("${swagger-ui.hostname}")
    private String hostname;

    @Bean
    public OpenAPI springShopOpenAPI() {
        List<Server> serversList = new ArrayList<>();

        if (StringUtils.isNoneBlank(hostname)) {
            serversList.add(new Server().url(hostname));
        }
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Spring Demo Application")
                        .version("1.0.0")
                        .description("Demo Application for User management Service.")
                        .license(new License().name("Apache 2.0").url("http://test.com"))
                        .contact(new Contact()
                                .name("Vishal")
                                .email("vgb@narola.email")
                                .url("http://www.testsite.com"))
                        )
                .servers(serversList);
    }
}
