package backend.bookSharing;

import backend.bookSharing.services.user.services.TokenValidation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import java.time.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*
  Does not use any annotation from spring or spring docs, so i am lead to believe it can be replaced
  with other annotations or application properties to set on the yaml
 */
@SecurityScheme(
        name = "Bearer",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer")
@OpenAPIDefinition(
        info = @Info(
                title = "Book lending",
                contact = @Contact(
                        name = "Eduardo Tavares",
                        email = "eduardodinis3@gmail.com"
                )
        ),
        servers = @Server(url = "http://localhost:8080"), security = @SecurityRequirement(name = "Bearer")
)
@SpringBootApplication
@EnableJpaRepositories()
public class Main {

    @Bean
    public TokenValidation.TokenValidTime tokenValidationBean(){
        return new TokenValidation.TokenValidTime(Duration.ofHours(10), Duration.ofMinutes(30));
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}