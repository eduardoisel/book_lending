package backend.bookSharing.http;

import backend.bookSharing.http.data.IsbnBody;
import backend.bookSharing.http.data.LendCreation;
import backend.bookSharing.http.data.RequestCreation;
import backend.bookSharing.services.book.failures.BookAdditionError;
import backend.bookSharing.services.book.BookService;
import backend.bookSharing.services.book.failures.BookLendError;
import backend.bookSharing.services.book.failures.BookRequestError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @GetMapping("hello")
    public ResponseEntity<?> helloWorld() {
        return ResponseEntity.status(200).body(String.format("Hello world, database has %d books\n", service.bookCount()));
    }

    @GetMapping("/owners/{bookId}")
    public ResponseEntity<?> getBookOwners(@PathVariable Integer bookId) {
        return ResponseEntity.status(200).body(String.format("Owners of book: %s \n", service.getOwnersOfBook(bookId).toString()));
    }


    @PostMapping("/addBook/isbn")
    public ResponseEntity<?> postBook(@RequestBody IsbnBody body) {

        try {
            service.addBookFromApi(body.isbn);

            return ResponseEntity.status(200).body("Book added");

        } catch (BookAdditionError error) {

            switch (error) {
                case BookAdditionError.Isbn10InUse isbn10InUse -> {
                    return ResponseEntity.status(400).body("Isbn 10 number already in use");
                }

                case BookAdditionError.Isbn13InUse isbn13InUse -> {
                    return ResponseEntity.status(400).body("Isbn 13 number already in use");
                }
            }

        }

    }

    @PostMapping("/request")
    public ResponseEntity<?> requestBook(@RequestBody RequestCreation body, @RequestHeader(value = "Authorization", required = true) String token) {
//        if (body.isbn() == null){
//            System.out.println("Should be impossible");
//        }

        try {
            service.requestBook(body.isbn(), body.ownerEmail(), token, body.timeInDays());

            return ResponseEntity.status(200).body("Request done");

        } catch (BookRequestError error) {

            return switch (error) {
                case BookRequestError.AlreadyRequested alreadyRequested ->
                        ResponseEntity.status(400).body("Already requested this book");
                case BookRequestError.CannotRequestFromSelf cannotRequestFromSelf ->
                        ResponseEntity.status(400).body("Owner cannot request its book");
                case BookRequestError.OwnershipNotFound ownershipNotFound ->
                        ResponseEntity.status(400).body(String.format("Book %s is not owned by user %s", body.isbn(), body.ownerEmail()));
                case BookRequestError.UserAuthenticationInvalid userAuthenticationInvalid ->
                        ResponseEntity.status(400).body("Authentication invalid");
            };
        }

    }


    @PostMapping("/lend")
    public ResponseEntity<?> lendBook(@RequestBody LendCreation body, @RequestHeader(value = "Authorization", required = true) String token) {

        try {
            service.lendBook(body.isbn(), body.receiverEmail(), token);

            return ResponseEntity.status(200).body("Book successfully lent");

        } catch (BookLendError error) {

            return switch (error) {
                case BookLendError.AlreadyLent alreadyLent ->
                        ResponseEntity.status(400).body("CAnnot lend already lent book");
                case BookLendError.RequestNotFound requestNotFound ->
                        ResponseEntity.status(400).body("Cannot lend to someone who did not request book");
                case BookLendError.UserAuthenticationInvalid userAuthenticationInvalid ->
                        ResponseEntity.status(400).body("Authentication invalid");
            };
        }


    }


}
