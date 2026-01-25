package backend.bookSharing.http;

import backend.bookSharing.ControllerTestBase;
import static io.restassured.RestAssured.given;

import backend.bookSharing.TestData;
import backend.bookSharing.http.data.IsbnBody;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;

//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@Transactional
public class BookControllerTest extends ControllerTestBase {


    @Test
    public void addBookByIsbn10(){

        given()
                .contentType(ContentType.JSON)
                .body(new IsbnBody(TestData.booksExclusiveFromApi[0].getIsbnTen()))
                .when()
                .post("/books/addBook/isbn")
                .then()
                .statusCode(201);

    }

    @Test
    public void addBookByIsbn13(){

        given()
                .contentType(ContentType.JSON)
                .body(new IsbnBody(TestData.booksExclusiveFromApi[0].getIsbnThirteen()))
                .when()
                .post("/books/addBook/isbn")
                .then()
                .statusCode(201);

    }


    @Test
    public void addBookTwice(){

        given()
                .contentType(ContentType.JSON)
                .body(new IsbnBody(TestData.booksExclusiveFromApi[0].getIsbnTen()))
                .when()
                .post("/books/addBook/isbn")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(new IsbnBody(TestData.booksExclusiveFromApi[0].getIsbnTen()))
                .when()
                .post("/books/addBook/isbn")
                .then()
                .statusCode(400);

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
