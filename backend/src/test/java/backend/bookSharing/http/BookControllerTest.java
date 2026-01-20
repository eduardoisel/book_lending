package backend.bookSharing.http;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
//@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private BookController controller;

    //@MockBean
    //private ExternalApiClient externalApiClient;

    @Test
    public void getBooksTest(){

    }
}
