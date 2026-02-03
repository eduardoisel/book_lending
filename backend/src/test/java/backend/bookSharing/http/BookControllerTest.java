package backend.bookSharing.http;

import static io.restassured.RestAssured.given;

import backend.bookSharing.TestData;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


public class BookControllerTest extends ControllerTestBase {

    @Test
    public void addBookByIsbn10(){

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/books/{isbn}", TestData.booksExclusiveFromApi[0].getIsbnTen())
                .then()
                .statusCode(201);

    }

    @Test
    public void addBookByIsbn13(){

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/books/{isbn}", TestData.booksExclusiveFromApi[0].getIsbnTen())
                .then()
                .statusCode(201);

    }


    @Test
    public void addBookTwice(){

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/books/{isbn}", TestData.booksExclusiveFromApi[0].getIsbnTen())
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/books/{isbn}", TestData.booksExclusiveFromApi[0].getIsbnTen())
                .then()
                .statusCode(400);

    }

}
