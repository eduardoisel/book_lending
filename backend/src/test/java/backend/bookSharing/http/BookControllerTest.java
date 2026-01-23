package backend.bookSharing.http;

import backend.bookSharing.ControllerTestBase;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;

//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;



public class BookControllerTest extends ControllerTestBase {

    @Autowired
    private BookController controller;


    @Test
    public void getBooksTest(){

    }

    @Test
    void testFindAll() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/books/hello")
                .then()
                .statusCode(200);

    }
}
