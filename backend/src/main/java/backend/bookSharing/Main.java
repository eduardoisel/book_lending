package backend.bookSharing;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.entities.Book;
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
            repository.save(new Book(null, null));
            repository.save(new Book(null, null));
            repository.save(new Book(10, null));
            repository.save(new Book(null, null));
            repository.save(new Book(null, null));

            // fetch all customers
            log.info("Books found with findAll():");
            log.info("-------------------------------");
            repository.findAll().forEach(book -> {
                log.info(book.toString());
            });
            log.info("");

            Integer id = 1;
            Book book = repository.findById(id).get();
            log.info("Book found with findById(1):");
            log.info("--------------------------------");
            log.info(book.toString());
            log.info("");

            log.info("--------------------------------------------");
            Book byIsbn = repository.findByIsbn10(10);

            log.info(byIsbn.toString());

            log.info("");
        };
    }
}