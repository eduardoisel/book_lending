package backend.bookSharing.http;

import backend.bookSharing.http.data.UserCreation;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.services.user.LogoutError;
import backend.bookSharing.services.user.OwnerShipAdditionError;
import backend.bookSharing.services.user.UserAuthenticationError;
import backend.bookSharing.services.user.UserCreationError;
import backend.bookSharing.services.user.UserService;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;


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

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserCreation body){

        Either<UserAuthenticationError, String> result = service.login(body.email, body.password);

        if (result.isLeft()){
            UserAuthenticationError error = result.getLeft();

            if (error instanceof UserAuthenticationError.UserOrPasswordAreInvalid){
                return ResponseEntity.status(400).body("User not recognized");
            }

        }

        return ResponseEntity
                .status(200)
                .body(String.format("Token: %s \n", result.get()));
    }

    /*
    todo change parameter input
     */
    @DeleteMapping("logout")
    public ResponseEntity<?> logout(@RequestBody String token){

        Either<LogoutError, Void> result = service.logout(token);

        if (result.isLeft()){
            LogoutError error = result.getLeft();

            if (error instanceof LogoutError.TokenInvalidForAuthentication){
                return ResponseEntity.status(400).body("User not recognized");
            }

        }

        return ResponseEntity
                .status(200)
                .body(String.format("Token: %s \n", result.get()));
    }

    @PostMapping("bookOwned/{isbn}")
    public ResponseEntity<?> addBookOwned(@PathVariable String isbn, @RequestHeader(value = "Authorization", required = true) String token){

        Either<OwnerShipAdditionError, Owned> result = service.addOwner(isbn, token);

        if (result.isLeft()){
            OwnerShipAdditionError error = result.getLeft();

            if (error instanceof OwnerShipAdditionError.UserAuthenticationInvalid){
                return ResponseEntity.status(401).body("User not recognized");
            }

            if (error instanceof OwnerShipAdditionError.BookNotFound){
                return ResponseEntity.status(404).body("Book from isbn not recognized");
            }

            if (error instanceof OwnerShipAdditionError.AlreadyMarkedAsOwned){
                return ResponseEntity.status(400).body("User already marked book as owned");
            }

        }

        return ResponseEntity
                .status(200)
                .body(String.format("Added as owned: %s \n", result.get()));
    }



}
