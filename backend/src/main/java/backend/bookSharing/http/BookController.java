package backend.bookSharing.http;

import backend.bookSharing.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService service;

    public BookController(BookService bookService) {
        this.service = bookService;
    }

    @GetMapping("hello")
    public ResponseEntity<?> helloWorld() {
        return ResponseEntity.status(200).body(String.format("Hello world, database has %d books\n", service.bookCount()));
    }

    @GetMapping("/owners/{bookId}")
    public ResponseEntity<?> bookOwners(@PathVariable Integer bookId) {
        return ResponseEntity.status(200).body(String.format("Owners of book: %s \n", service.getOwnersOfBook(bookId).toString()));
    }

}
