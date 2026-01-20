package backend.bookSharing;

import backend.bookSharing.services.book.MockBookApi;
import backend.bookSharing.services.book.api.BookApi;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
/**
 * Replaces default beans from app
 */
public class MockUsage {

    /*
     Used to avoid external API usage repeatedly from tests
     */
    @Bean
    public BookApi mockBookApi(){
        return new MockBookApi();
    }

}
