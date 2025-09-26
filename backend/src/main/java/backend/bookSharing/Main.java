package backend.bookSharing;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.entities.book.Book;
import backend.bookSharing.repository.entities.book.Language;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Bean
    public CommandLineRunner demo(BookRepository repository) {
        return (args) -> {
            // save a few books
            repository.save(new Book(null, null, null, Language.English));
            repository.save(new Book(null, null, null, Language.Spanish));
            repository.save(new Book(null, null, null, Language.Portuguese));
            repository.save(new Book(null, null, null, Language.Portuguese));
            repository.save(new Book(null, null, null, Language.Portuguese));

            // fetch all customers
            log.info("Books found with findAll():");
            log.info("-------------------------------");
            repository.findAll().forEach(book -> {
                log.info(book.toString());
            });
            log.info("");

            /*
            log.info("Books found with findByLanguage():");
            repository.findByLanguage(Language.Portuguese).forEach(book -> {
                log.info(book.toString());
            });

            log.info("");
             */

        };
    }
}