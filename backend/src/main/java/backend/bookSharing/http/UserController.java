package backend.bookSharing.http;

import backend.bookSharing.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
/**
 * Handles requests related to users, whether its others or the authenticated user
 */
public class UserController {

    @Autowired
    private UserService service;

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

}
