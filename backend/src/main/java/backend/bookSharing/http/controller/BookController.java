package backend.bookSharing.http.controller;

import backend.bookSharing.http.data.LendCreation;
import backend.bookSharing.http.data.RequestCreation;
import backend.bookSharing.http.returns.ListedData;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.book.failures.BookAdditionError;
import backend.bookSharing.services.book.BookService;
import backend.bookSharing.services.book.failures.BookLendError;
import backend.bookSharing.services.book.failures.BookOwnersSearchError;
import backend.bookSharing.services.book.failures.BookRequestError;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    /**
     * General list book search by segments
     * @param page optional parameter (defaulted to 0) to indicate the page
     * @return list of books
     */
    @GetMapping("/")
    @Cacheable(
            value = "getBooks",
            unless = "#result.getBody().hasNextPage()==false")//no cache since list may increase or bool change
    public ResponseEntity<?> getBooks(@RequestParam(required = false, defaultValue = "0") Integer page){

        Page<Book> books = service.getBooks(page);

        ListedData body = new ListedData(books.toList().toArray(), books.hasNext(), books.hasPrevious());

        val ret = ResponseEntity.status(HttpStatus.OK).body(body);

        return ret;
    }

    @GetMapping("/owners/{isbn}")
    @Cacheable("bookOwners")
    public ResponseEntity<?> getBookOwners(@PathVariable String isbn, @RequestParam(required = false, defaultValue = "0") Integer page) {

        try {
            Page<User> users = service.getOwnersOfBook(isbn, page);

            ListedData body = new ListedData(users.toList().toArray(), users.hasNext(), users.hasPrevious());

            return ResponseEntity.status(HttpStatus.OK).body(body);

        } catch (BookOwnersSearchError error) {

            return switch (error) {
                case BookOwnersSearchError.BookNotFound bookNotFound ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book does not exist");
            };

        }

    }


    @PostMapping("/{isbn}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> postBook(@PathVariable String isbn) {

        try {
            Book book = service.addBookFromApi(isbn);

            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        } catch (BookAdditionError e) {
            return switch (e) {
                case BookAdditionError.Isbn10InUse isbn10InUse ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getClass().getSimpleName());
                case BookAdditionError.Isbn13InUse isbn13InUse ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getClass().getSimpleName());
                case BookAdditionError.BookNotFound bookNotFound ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getClass().getSimpleName());
            };

        }

    }

    @PostMapping("/request")
    public ResponseEntity<?> requestBook(@RequestBody RequestCreation body, @RequestHeader(value = "Authorization", required = true) String token) {
//        if (body.isbn() == null){
//            System.out.println("Should be impossible");
//        }

        try {
            service.requestBook(body.isbn(), body.ownerEmail(), token, body.timeInDays());

            return ResponseEntity.status(HttpStatus.OK).body("Request done");

        } catch (BookRequestError error) {

            return switch (error) {
                case BookRequestError.AlreadyRequested alreadyRequested ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already requested this book");
                case BookRequestError.CannotRequestFromSelf cannotRequestFromSelf ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Owner cannot request its book");
                case BookRequestError.OwnershipNotFound ownershipNotFound ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Book %s is not owned by user %s", body.isbn(), body.ownerEmail()));
                case BookRequestError.UserAuthenticationInvalid userAuthenticationInvalid ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authentication invalid");
            };
        }

    }


    @PostMapping("/lend")
    public ResponseEntity<?> lendBook(@RequestBody LendCreation body, @RequestHeader(value = "Authorization", required = true) String token) {

        try {
            service.lendBook(body.isbn(), body.receiverEmail(), token);

            return ResponseEntity.status(HttpStatus.OK).body("Book successfully lent");

        } catch (BookLendError error) {

            return switch (error) {
                case BookLendError.AlreadyLent alreadyLent ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CAnnot lend already lent book");
                case BookLendError.RequestNotFound requestNotFound ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot lend to someone who did not request book");
                case BookLendError.UserAuthenticationInvalid userAuthenticationInvalid ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authentication invalid");
            };
        }


    }


}
