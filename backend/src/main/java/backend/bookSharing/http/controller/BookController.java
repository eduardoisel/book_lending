package backend.bookSharing.http.controller;

import backend.bookSharing.http.data.BookDto;
import backend.bookSharing.http.data.IsbnString;
import backend.bookSharing.http.data.LendCreation;
import backend.bookSharing.http.data.RequestCreation;
import backend.bookSharing.http.data.UserDto;
import backend.bookSharing.http.returns.ListedData;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.book.BookService;
import backend.bookSharing.services.book.failures.BookAdditionError;
import backend.bookSharing.services.book.failures.BookLendError;
import backend.bookSharing.services.book.failures.BookOwnersSearchError;
import backend.bookSharing.services.book.failures.BookRequestError;
import backend.bookSharing.utils.IsbnUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
@Validated
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
    public ListedData<BookDto> getBooks(
            @RequestParam(required = false, defaultValue = "0") Integer page) {

        Page<BookDto> books = service.getBooks(page).map(BookDto::new);

        return new ListedData<>(books);
    }

    /**
     * Get owners of a specific book
     *
     * @param ISBN ISBN of book in question
     * @param page page number, starts at 0
     * @param user user doing the request
     * @return list of books
     */
    @GetMapping("/owners/{ISBN}")
    // @Cacheable("bookOwners")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ListedData<UserDto> getBookOwners(
            @Valid @PathVariable IsbnString ISBN,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @AuthenticationPrincipal User user)
            throws BookOwnersSearchError {

        Page<UserDto> users =
                service.getOwnersOfBook(ISBN.ISBN13(), page, user.getLocation()).map(UserDto::new);

        return new ListedData<>(users);
    }

    /**
     *
     * todo: to remove, service should check and add the book to the database by itself whenever specifically requested
     * Add a book to the system so it can be recognized, by giving an id and letting the server check if it exists
     *
     * @param ISBN ISBN to identify book
     * @return Added book
     * @throws BookAdditionError If given ISBN is in use or not able to be confirmed as an existing book
     */
    @PostMapping("/{ISBN}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public BookDto postBook(@Valid @PathVariable IsbnString ISBN) throws BookAdditionError {
        return new BookDto(service.addBookFromApi(ISBN.ISBN13()));
    }

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.OK)
    public void requestBook(
            @Valid @RequestBody RequestCreation body,
            @AuthenticationPrincipal User authenticatedUser)
            throws BookRequestError {

        service.requestBook(
                IsbnUtils.getIsbn13(body.isbn()),
                body.ownerEmail(),
                authenticatedUser,
                body.timeInDays());
    }

    @PostMapping("/lend")
    @ResponseStatus(HttpStatus.OK)
    public void lendBook(@Valid @RequestBody LendCreation body, @AuthenticationPrincipal User user)
            throws BookLendError {

        service.lendBook(IsbnUtils.getIsbn13(body.isbn()), body.receiverEmail(), user);
    }
}
