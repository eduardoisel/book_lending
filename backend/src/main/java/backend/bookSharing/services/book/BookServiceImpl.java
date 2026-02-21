package backend.bookSharing.services.book;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.LendRepository;
import backend.bookSharing.repository.OwnedRepository;
import backend.bookSharing.repository.RequestRepository;
import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Lend;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.OwnedId;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.RequestId;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.book.api.BookApi;
import backend.bookSharing.services.book.failures.BookAdditionError;
import backend.bookSharing.services.book.failures.BookLendError;
import backend.bookSharing.services.book.failures.BookOwnersSearchError;
import backend.bookSharing.services.book.failures.BookRequestError;
import backend.bookSharing.services.user.UserService;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final UserService userService; //for auth check. Consider changing place

    private final OwnedRepository ownedRepo;

    private final BookRepository bookRepo;

    private final UserRepository userRepo;

    private final RequestRepository requestRepo;

    private final LendRepository lendRepo;

    private final BookApi bookApi;

    @Override
    public Page<Book> getBooks(Integer pageNumber) {
        return bookRepo.findAll(PageRequest.of(pageNumber, 20));
    }

    public Page<User> getOwnersOfBook(String isbn, Integer pageNumber) throws BookOwnersSearchError {

        Book book = isbn.length() == 10 ? bookRepo.findByIsbnTen(isbn) : bookRepo.findByIsbnThirteen(isbn);

        if (book == null) {
            throw new BookOwnersSearchError.BookNotFound();
        }

        Page<Owned> ownedPage = ownedRepo.findByBookId(
                book.getId(),
                PageRequest.of(
                        pageNumber,
                        20 //, Sort.by("book_id")
                ));

        return ownedPage.map(Owned::getUser);

    }

    public Book addBookFromApi(String isbn) throws BookAdditionError {

        boolean isIsbn10 = isbn.length() == 10;

        if (isIsbn10 && bookRepo.findByIsbnTen(isbn) != null) {
            throw new BookAdditionError.Isbn10InUse();
        }

        if (!isIsbn10 && bookRepo.findByIsbnThirteen(isbn) != null) {
            throw new BookAdditionError.Isbn13InUse();
        }

        Book book = bookApi.getBook(isbn);

        if (book == null) {
            throw new BookAdditionError.BookNotFound();
        }

        return bookRepo.save(book);

    }

    @Override
    public void requestBook(String isbn, String ownerEmail, String token, Integer timeInDays) throws BookRequestError {

        User requester = userService.checkAuthentication(token);

        if (requester == null) {
            throw new BookRequestError.UserAuthenticationInvalid();
        }

        User owner = userRepo.findByEmail(ownerEmail)
                .orElseThrow((Supplier<BookRequestError>) BookRequestError.OwnershipNotFound::new);

        if (requester.getId().equals(owner.getId())) {
            throw new BookRequestError.CannotRequestFromSelf();
        }

        Book searchedBook = isbn.length() == 10 ? bookRepo.findByIsbnTen(isbn) : bookRepo.findByIsbnThirteen(isbn);

        if (searchedBook == null) {
            throw new BookRequestError.OwnershipNotFound();
        }

        OwnedId ownedIdCheck = new OwnedId(owner.getId(), searchedBook.getId());

        Owned owned = ownedRepo.findById(ownedIdCheck)
                .orElseThrow((Supplier<BookRequestError>) BookRequestError.OwnershipNotFound::new);

        if (requestRepo.findById(new RequestId(ownedIdCheck, requester.getId())).isPresent()) {
            throw new BookRequestError.AlreadyRequested();
        }

        requestRepo.save(new Request(owned, requester.getId(), timeInDays));
    }

    @Override
    public void lendBook(String isbn, String receiverEmail, String token) throws BookLendError {
        User lender = userService.checkAuthentication(token);

        if (lender == null) {
            throw new BookLendError.UserAuthenticationInvalid();
        }

        User requester = userRepo.findByEmail(receiverEmail)
                .orElseThrow((Supplier<BookLendError>) BookLendError.RequestNotFound::new);

        Book book = isbn.length() == 10 ? bookRepo.findByIsbnTen(isbn) : bookRepo.findByIsbnThirteen(isbn);

        if (book == null) {
            throw new BookLendError.RequestNotFound();
        }

        OwnedId ownedIdCheck = new OwnedId(lender.getId(), book.getId());

        //use if error message change to specify where went wrong (i.e. no book, no of ownership of book instead of no request)
        ownedRepo.findById(ownedIdCheck).orElseThrow((Supplier<BookLendError>) BookLendError.RequestNotFound::new);

        Request request = requestRepo.findById(new RequestId(ownedIdCheck, requester.getId()))
                .orElseThrow((Supplier<BookLendError>) BookLendError.RequestNotFound::new);

        if (lendRepo.existsById(ownedIdCheck)) {
            throw new BookLendError.AlreadyLent();
        }

        lendRepo.save(new Lend(request)); //mark book as lent

        requestRepo.delete(request); //remove from request list

    }


}
