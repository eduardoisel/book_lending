package backend.bookSharing;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.services.user.services.TokenValidation;
import com.emedina.transactional.config.TransactionManagerConfigWithEither;
import com.emedina.transactional.support.SpringTransactionAnnotationParserWithEither;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;

@SpringBootApplication
@EnableJpaRepositories()
public class Main {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);


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
        return new TokenValidation.TokenValidTime(Duration.ofSeconds(10), Duration.ofMinutes(30));
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    //runs on start if bean is not commented
    //@Bean
    public CommandLineRunner demo(BookRepository repository) {
        return (args) -> {
            // save a few books

            System.out.println("\n\nP ERSONAL PRINT\n!\n\n aa\n");

            repository.save(new Book(null, null, "Title1", Book.Language.English));
            repository.save(new Book(null, null, "Title2", Book.Language.Spanish));
            repository.save(new Book(null, null, "Title3", Book.Language.Portuguese));
            repository.save(new Book(null, null, "Title4", Book.Language.Portuguese));
            repository.save(new Book(null, null, "Title5", Book.Language.Portuguese));



            // fetch all customers
            log.info("Books found with findAll():");
            log.info("-------------------------------");
            repository.findAll().forEach(book -> {
                log.info(book.toString());
            });
            log.info("");

        };
    }

}