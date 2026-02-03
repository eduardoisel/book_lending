package backend.bookSharing.http;

import backend.bookSharing.services.ServiceTestBase;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract public class ControllerTestBase extends ServiceTestBase {

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void setup(){

        RestAssured.baseURI = "http://localhost:" + port;

    }
}
