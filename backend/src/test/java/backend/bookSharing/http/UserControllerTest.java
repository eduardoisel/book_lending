package backend.bookSharing.http;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

//import com.testcontainers.examples.dto.CreateUserDto;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

//from
//https://dev.to/mspilari/integration-tests-on-spring-boot-with-postgresql-and-testcontainers-4dpc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UserControllerTest {

    //static class added
    static class CreateUserDto{
        public CreateUserDto(String username){

        }
    }

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @Value("${local.server.port}")
    private int port;

    private String createUserAndGetId(String userName) {
        var newUser = new CreateUserDto(userName);

        return given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .port(port)
                .when()
                .post("/users/")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    @Test
    void shouldCreateUser() {
        var newUser = new CreateUserDto("Test user");

        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .port(port)
                .when()
                .post("/users/")
                .then()
                .statusCode(201)
                .body("name", equalTo("Test user"));
    }

    @Test
    void shouldDeleteUserById() {
        String userId = createUserAndGetId("Test user");

        given()
                .port(port)
                .when()
                .delete("/users/{id}", userId)
                .then()
                .statusCode(204)
                .body(emptyOrNullString());
    }

    @Test
    void shouldGetAllUsers() {
        createUserAndGetId("Test user");

        given()
                .port(port)
                .when()
                .get("/users/")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void shouldGetUserById() {
        String userId = createUserAndGetId("Test user");

        given()
                .port(port)
                .when()
                .get("/users/{id}", userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Test user"));

    }

    @Test
    void shouldReturn500WhenSearchForNonExistentUser() {
        String nonExistentUserId = "9999";

        given()
                .port(port)
                .when()
                .get("/users/{id}", nonExistentUserId)
                .then()
                .statusCode(500);
    }

    @Test
    void shouldReturn500WhenDeletingNonExistentUser() {
        String nonExistentUserId = "9999";

        given()
                .port(port)
                .when()
                .delete("/users/{id}", nonExistentUserId)
                .then()
                .statusCode(500);
    }
}