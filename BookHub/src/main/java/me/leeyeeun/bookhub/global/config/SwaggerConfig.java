package me.leeyeeun.bookhub.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(info())
                .servers(servers());
    }

    private Info info() {
        return new Info()
                .title("BookHub API")
                .description("BookHub 서버")
                .version("1.0.0");
    }

    private List<Server> servers() {
        return List.of(new Server()
                .url("http://localhost:8080") // Todo: 배포 후 수정하기
                .description("Configured Server")
        );
    }
}
