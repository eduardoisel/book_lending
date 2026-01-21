package backend.bookSharing;

import backend.bookSharing.services.book.MockBookApi;
import backend.bookSharing.services.book.api.BookApi;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@TestConfiguration
/**
 * Replaces default beans from app
 *
 * For beans to be recognized, class needs {@link Import}
 */
/*
 Todo review: these beans do not take priority by default, check if configuration is wrong later. Solved for now with @Primary as sugested on link below.
 https://codingtechroom.com/question/mockbean-no-unique-bean-definition-exception-spring-boot

 From the stackoverflow link below (using @MockBean instead), could be a bug that appeared again? Do not have the knowledge to be sure
 https://stackoverflow.com/questions/39431630/mockbean-annotation-in-spring-boot-test-causes-nouniquebeandefinitionexception

 This test structure may be scrapped afterwards, so it will be left like this for now
 */
public class MockUsage {

    /*
     Used to avoid external API usage repeatedly from tests
     */
    @Bean
    @Primary
    public BookApi mockBookApi(){
        return new MockBookApi();
    }

}
