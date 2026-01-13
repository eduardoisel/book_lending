package backend.bookSharing.http;

import backend.bookSharing.http.data.UserCreation;
import backend.bookSharing.services.user.UserCreationError;
import backend.bookSharing.services.user.UserService;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService service;

    public UserController(UserService userService) {
        this.service = userService;
    }

    @GetMapping("owned/{userId}")
    public ResponseEntity<?> bookOwners(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(String.format("Books owned: %s \n", service.getOwnedBooks(userId).toString()));
    }

    @GetMapping("owned/{userId}/requests/{bookId}")
    public ResponseEntity<?> bookRequests(@PathVariable Integer userId, @PathVariable Integer bookId) {
        return ResponseEntity.status(200).body(String.format("Requests of book: %s \n", service.getRequestsOfBook(userId, bookId).toString()));
    }

    @GetMapping("owned/{userId}/lend/{bookId}")
    public ResponseEntity<?> bookLend(@PathVariable Integer userId, @PathVariable Integer bookId) {
        return ResponseEntity.status(200).body(String.format("Current lend of book: %s \n", service.getLendOfBook(userId, bookId).toString()));
    }

    @PostMapping("createUser")
    public ResponseEntity<?> createUser(@RequestBody UserCreation body){

        Either<UserCreationError, Integer> result = service.createUser(body.email, body.password);

        if (result.isLeft()){
            UserCreationError error = result.getLeft();

            if (error instanceof UserCreationError.WeakPassword){
                return ResponseEntity.status(400).body("Password is weak. Todo add ways to inform requirements");
            }

            if (error instanceof  UserCreationError.EmailInUse){
                return ResponseEntity.status(400).body("Email is already in use for this service.");
            }
        }

        return ResponseEntity
                .status(200)
                .body(String.format("Id of user: %s \n", result.get()));
    }

}
