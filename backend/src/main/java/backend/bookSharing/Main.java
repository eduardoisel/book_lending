package backend.bookSharing;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.entities.book.Book;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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