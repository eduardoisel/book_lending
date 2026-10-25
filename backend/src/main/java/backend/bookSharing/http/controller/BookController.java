package backend.bookSharing.http.controller;

import backend.bookSharing.http.data.LendCreation;
import backend.bookSharing.http.data.RequestCreation;
import backend.bookSharing.http.returns.ListedData;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.book.BookService;
import backend.bookSharing.services.book.failures.BookAdditionError;
import backend.bookSharing.services.book.failures.BookLendError;
import backend.bookSharing.services.book.failures.BookOwnersSearchError;
import backend.bookSharing.services.book.failures.BookRequestError;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    /**
     * General list book search by segments
     *
     * @param page optional parameter (defaulted to 0) to indicate the page
     * @return list of books
     */
    @GetMapping("/")
//    @Cacheable(
//            value = "getBooks",
//            unless = "#result.hasNext()==false") // no cache since list may increase
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ListedData<Book> getBooks(@RequestParam(required = false, defaultValue = "0") Integer page) {

        Page<Book> books = service.getBooks(page);

        List<Book> list= books.toList();

        return new ListedData<Book>(list, books.hasNext(), books.hasPrevious());
    }

    /**
     * Get owners of a specific book
     *
     * @param isbn ISBN of book in question
     * @param page page number, starts at 0
     * @param user
     * @return list of books
     * @throws BookOwnersSearchError
     */
    @GetMapping("/owners/{ISBN}")
    // @Cacheable("bookOwners")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ListedData<User> getBookOwners(
            @PathVariable String isbn,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @AuthenticationPrincipal User user)
            throws BookOwnersSearchError {

        Page<User> users = service.getOwnersOfBook(isbn, page, user.getLocation());

        return new ListedData<User>(users.toList(), users.hasNext(), users.hasPrevious());
    }

    /**
     * Add a book to the system so it can be recognized, by giving an id and letting the server check if it exists
     *
     * @param ISBN ISBN to identify book
     * @return Added book
     * @throws BookAdditionError If given ISBN is in use or not able to be confirmed as an existing book
     */
    @PostMapping("/{ISBN}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Book postBook(@PathVariable String ISBN) throws BookAdditionError {
        return service.addBookFromApi(ISBN);
    }

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.OK)
    public void requestBook(
            @RequestBody RequestCreation body, @AuthenticationPrincipal User authenticatedUser)
            throws BookRequestError {

        service.requestBook(body.isbn(), body.ownerEmail(), authenticatedUser, body.timeInDays());
    }

    @PostMapping("/lend")
    @ResponseStatus(HttpStatus.OK)
    public void lendBook(@RequestBody LendCreation body, @AuthenticationPrincipal User user)
            throws BookLendError {

        service.lendBook(body.isbn(), body.receiverEmail(), user);
    }
}
