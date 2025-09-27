package backend.bookSharing;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.entities.book.Book;
import backend.bookSharing.repository.entities.book.Language;
import javax.sql.DataSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@SpringBootApplication
public class Main {

    /*
    @Bean
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("schema.sql", "test-data.sql")
                .build();
    }
    */

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Bean
    public CommandLineRunner demo(BookRepository repository) {
        return (args) -> {
            // save a few books

            /*

            System.out.println("\n\nP ERSONAL PRINT\n!\n\n aa\n");
            repository.save(new Book(null, null, "Title1", Language.English));
            repository.save(new Book(null, null, "Title2", Language.Spanish));
            repository.save(new Book(null, null, "Title3", Language.Portuguese));
            repository.save(new Book(null, null, "Title4", Language.Portuguese));
            repository.save(new Book(null, null, "Title5", Language.Portuguese));

             */

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