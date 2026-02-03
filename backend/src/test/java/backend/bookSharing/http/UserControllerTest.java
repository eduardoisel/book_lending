package backend.bookSharing.http;

//import com.testcontainers.examples.dto.CreateUserDto;


public class UserControllerTest extends ControllerTestBase {

    /*

    private String createUserAndGetId(String userName) {
        var newUser = new CreateUserDto(userName);

        return given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post("/users/")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    @Test
    void shouldCreateUser() {
        var newUser = new UserCreation("test@gmail.com", "passWORD_123");

        given()
                .body(newUser)
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
                .when()
                .get("/users/{id}", nonExistentUserId)
                .then()
                .statusCode(500);
    }

    @Test
    void shouldReturn500WhenDeletingNonExistentUser() {
        String nonExistentUserId = "9999";

        given()
                .when()
                .delete("/users/{id}", nonExistentUserId)
                .then()
                .statusCode(500);
    }
     */
}