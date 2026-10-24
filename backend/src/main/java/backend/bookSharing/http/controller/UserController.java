package backend.bookSharing.http.controller;

import backend.bookSharing.http.returns.ListedData;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.UserService;
import backend.bookSharing.services.user.failures.OwnerShipAdditionError;
import backend.bookSharing.services.user.failures.OwnershipRequestSearchError;
import backend.bookSharing.services.user.failures.UserLockingError;
import backend.bookSharing.services.user.failures.UserOwnershipSearchError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    /**
     * Get list of books owned by a user
     *
     * @param userId id of user in question
     * @param page   page number. First is 0
     * @return list of owned books
     */
    @GetMapping("owned/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ListedData booksOwned(
            @PathVariable Integer userId,
            @RequestParam(required = false, defaultValue = "0") Integer page)
            throws UserOwnershipSearchError {

        Page<Book> search = service.getOwnedBooks(userId, page);

        return new ListedData(search.toList().toArray(), search.hasNext(), search.hasPrevious());
    }

    /**
     * Gets requests of book owned by user
     *
     * @param userId id of user that owns the book
     * @param bookId id of owned book
     * @param page   pagination parameter. First page is 0
     * @return list of requests
     */
    @GetMapping("owned/{userId}/requests/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ListedData bookRequests(
            @PathVariable Integer userId,
            @PathVariable Integer bookId,
            @RequestParam(required = false, defaultValue = "0") Integer page)
            throws OwnershipRequestSearchError {

        Page<Request> search = service.getRequestsOfBook(userId, bookId, page);

        return new ListedData(search.toList().toArray(), search.hasNext(), search.hasPrevious());
    }

    @PostMapping("owned/{ISBN}")
    @ResponseStatus(value = HttpStatus.OK)
    public void addBookOwned(
            @PathVariable String ISBN, @AuthenticationPrincipal User authenticatedUser)
            throws OwnerShipAdditionError {

        service.addOwner(ISBN, authenticatedUser);
    }

    @PostMapping("blacklist/{email}")
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addBlacklist(@PathVariable String email) throws UserLockingError {
        service.lockAccount(email);
    }
}
