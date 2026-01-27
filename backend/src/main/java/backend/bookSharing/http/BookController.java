package backend.bookSharing.http;

import backend.bookSharing.http.data.IsbnBody;
import backend.bookSharing.services.book.failures.BookAdditionError;
import backend.bookSharing.services.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

            return ResponseEntity.status(400).body("Isbn 13 number already in use");

        } catch (BookAdditionError error) {

            switch (error){
                case BookAdditionError.Isbn10InUse isbn10InUse -> {
                    return ResponseEntity.status(400).body("Isbn 10 number already in use");
                }

                case BookAdditionError.Isbn13InUse isbn13InUse -> {
                    return ResponseEntity.status(400).body("Isbn 10 number already in use");
                }
            }

        }

    }



}
