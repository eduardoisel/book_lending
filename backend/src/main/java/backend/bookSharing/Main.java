package backend.bookSharing;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Token;
import backend.bookSharing.services.user.services.TokenValidation;
import java.time.Duration;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);


    @Bean
    public TokenValidation tokenValidationBean(){
        return new TokenValidation(Duration.ofHours(10), Duration.ofMinutes(30));
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

    //@Bean
    public CommandLineRunner dem(BookRepository repository) {
        return (args) -> {
            // save a few books

            repository.save(new Book(1234567899, null, "Title1", Book.Language.English));

            System.out.println("\n\n Book saved\n\n");

            Book b = repository.findByIsbnTen(1234567899);
            System.out.printf("Is b null: %b\n", b == null);

            Book nu = repository.findByIsbnTen(1234567899);
            System.out.printf("Is nu null: %b", nu == null); if (nu != null){System.out.printf("id: %d\n", nu.getId());}

        };
    }
}