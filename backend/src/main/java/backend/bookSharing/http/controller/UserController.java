package backend.bookSharing.http.controller;

import backend.bookSharing.http.data.UserCreation;
import backend.bookSharing.http.returns.ListedData;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.failures.LogoutError;
import backend.bookSharing.services.user.failures.OwnerShipAdditionError;
import backend.bookSharing.services.user.failures.OwnershipRequestSearchError;
import backend.bookSharing.services.user.failures.UserAuthenticationError;
import backend.bookSharing.services.user.failures.UserCreationError;
import backend.bookSharing.services.user.UserService;
import backend.bookSharing.services.user.failures.UserOwnershipSearchError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    /**
     * Get list of books owned by a user
     * @param userId id of user in question
     * @param page page number. First is 0
     * @return on success status 200 and info in body
     */
    @GetMapping("owned/{userId}")
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> booksOwned(@PathVariable Integer userId, @RequestParam(required = false, defaultValue = "0") Integer page) {

        try {
            Page<Book> search = service.getOwnedBooks(userId, page);

            return ResponseEntity.status(200)
                    .body(new ListedData(search.toList().toArray(), search.hasNext(), search.hasPrevious()));

        }catch (UserOwnershipSearchError _){
            return ResponseEntity.status(400).body("User does not exist");
        }

    }

    /**
     *  Gets requests of book owned by user
     * @param userId id of user that owns the book
     * @param bookId id of owned book
     * @param page pagination parameter. First page is 0
     * @return success or failure
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "400", description = "User does not own book"),
            @ApiResponse(responseCode = "200", description = "Successfully retrieved instances of requests of owned book by user")
    })
    @GetMapping("owned/{userId}/requests/{bookId}")
    public ResponseEntity<?> bookRequests(@PathVariable Integer userId, @PathVariable Integer bookId, @RequestParam(required = false, defaultValue = "0") Integer page) {

        try {
            Page<Request> search = service.getRequestsOfBook(userId, bookId, page);

            return ResponseEntity.status(200)
                    .body(new ListedData(search.toList().toArray(), search.hasNext(), search.hasPrevious()));

        }catch (OwnershipRequestSearchError _){
            return ResponseEntity.status(400).body("User does not own the book");
        }

    }

    @PostMapping("bookOwned/{isbn}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> addBookOwned(@PathVariable String isbn, User authenticatedUser) {

        try {
            Owned result = service.addOwner(isbn, authenticatedUser);


            return ResponseEntity.status(200).body(String.format("Added as owned: %s \n", result));

        } catch (OwnerShipAdditionError error) {
            return switch (error) {

                case OwnerShipAdditionError.BookNotFound bookNotFound ->
                        ResponseEntity.status(404).body("Book from isbn not recognized");

                case OwnerShipAdditionError.AlreadyMarkedAsOwned alreadyMarkedAsOwned ->
                        ResponseEntity.status(400).body("User already marked book as owned");

            };

        }

    }


}
