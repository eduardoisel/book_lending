package backend.bookSharing.http;

import backend.bookSharing.http.data.UserCreation;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.services.user.failures.LogoutError;
import backend.bookSharing.services.user.failures.OwnerShipAdditionError;
import backend.bookSharing.services.user.failures.UserAuthenticationError;
import backend.bookSharing.services.user.failures.UserCreationError;
import backend.bookSharing.services.user.UserService;
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
    public ResponseEntity<?> createUser(@RequestBody UserCreation body) {
        try {
            Integer result = service.createUser(body.email, body.password);

            return ResponseEntity
                    .status(200)
                    .body(String.format("Id of user: %s \n", result));

        } catch (UserCreationError error) {

            if (error instanceof UserCreationError.WeakPassword) {
                return ResponseEntity.status(400).body("Password is weak. Todo add ways to inform requirements");
            }

            if (error instanceof UserCreationError.EmailInUse) {
                return ResponseEntity.status(400).body("Email is already in use for this service.");
            }

            throw new RuntimeException("Should not be here");
        }

    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserCreation body) {
        try {
            String result = service.login(body.email, body.password);

            return ResponseEntity
                    .status(200)
                    .body(String.format("Token: %s \n", result));

        } catch (UserAuthenticationError error) {

            if (error instanceof UserAuthenticationError.UserOrPasswordAreInvalid) {
                return ResponseEntity.status(400).body("User not recognized");
            }

            throw new RuntimeException("Should not be here");

        }

    }

    /*
    todo change parameter input
     */
    @DeleteMapping("logout")
    public ResponseEntity<?> logout(@RequestBody String token) {
        try {
            service.logout(token);

            return ResponseEntity
                    .status(200)
                    .body("Deleted token");

        } catch (LogoutError error) {

            if (error instanceof LogoutError.TokenInvalidForAuthentication) {
                return ResponseEntity.status(400).body("User not recognized");
            }

            throw new RuntimeException("Should not be here");
        }

    }

    @PostMapping("bookOwned/{isbn}")
    public ResponseEntity<?> addBookOwned(@PathVariable String isbn, @RequestHeader(value = "Authorization", required = true) String token) {

        try {
            Owned result = service.addOwner(isbn, token);

            return ResponseEntity
                    .status(200)
                    .body(String.format("Added as owned: %s \n", result));

        } catch (OwnerShipAdditionError error) {
            switch (error) {
                case OwnerShipAdditionError.UserAuthenticationInvalid userAuthenticationInvalid -> {
                    return ResponseEntity.status(401).body("User not recognized");
                }
                case OwnerShipAdditionError.BookNotFound bookNotFound -> {
                    return ResponseEntity.status(404).body("Book from isbn not recognized");
                }
                case OwnerShipAdditionError.AlreadyMarkedAsOwned alreadyMarkedAsOwned -> {
                    return ResponseEntity.status(400).body("User already marked book as owned");
                }
                default -> throw new RuntimeException("Should not get here");
            }

        }

    }


}
