package backend.bookSharing;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.services.user.services.TokenValidation;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;

@SpringBootApplication
@EnableJpaRepositories()
public class Main {

    private static class EmptySecurityFilterChain implements SecurityFilterChain{

        @Override
        public boolean matches(HttpServletRequest request) {
            return false;
        }

        @Override
        public List<Filter> getFilters() {
            return List.of();
        }
    }


    /*
    https://docs.spring.io/spring-boot/reference/web/spring-security.html
    Not placing this will require authentication by html form
    Substitutes the default by one that does not have any security.
    Todo review the link when working fully on http side
     */
    @Bean
    public SecurityFilterChain securityFilterChainBean(){
        return new EmptySecurityFilterChain();

    }

    @Bean
    public TokenValidation.TokenValidTime tokenValidationBean(){
        return new TokenValidation.TokenValidTime(Duration.ofHours(10), Duration.ofMinutes(30));
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}