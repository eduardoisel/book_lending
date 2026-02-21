package backend.bookSharing.unit.services;

import backend.bookSharing.RandomValuesGenerator;
import backend.bookSharing.TestData;
import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.LendRepository;
import backend.bookSharing.repository.OwnedRepository;
import backend.bookSharing.repository.RequestRepository;
import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.book.BookServiceImpl;
import backend.bookSharing.services.book.api.BookApi;
import backend.bookSharing.services.book.failures.BookAdditionError;
import backend.bookSharing.services.book.failures.BookOwnersSearchError;
import backend.bookSharing.services.user.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@RepositoryMocks
public class BookServiceTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private OwnedRepository ownedRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RequestRepository requestRepo;

    @Autowired
    private LendRepository lendRepo;// = Mockito.mock(LendRepository.class);

    @Autowired
    private BookApi bookApi;// = Mockito.mock(BookApi.class);

    @InjectMocks
    private BookServiceImpl service;

    @Test
    public void addBookFromApiTest() throws BookAdditionError {
        Book book = TestData.booksExclusiveFromApi[0];

        when(bookRepo.findByIsbnTen(anyString())).thenReturn(null);
        when(bookApi.getBook(book.getIsbnTen())).thenReturn(book);


        service.addBookFromApi(book.getIsbnTen());
    }

    @Test
    public void addRepeatedBookToDatabase() {

        Book book = TestData.databaseBooks[0];

        when(bookApi.getBook(any())).thenReturn(book);

        when(bookRepo.findByIsbnTen(anyString())).thenReturn(book);

        assertThrowsExactly(
                BookAdditionError.Isbn10InUse.class,
                () -> service.addBookFromApi(book.getIsbnTen())
        );

        when(bookRepo.findByIsbnThirteen(anyString())).thenReturn(book);

        assertThrowsExactly(
                BookAdditionError.Isbn13InUse.class,
                () -> service.addBookFromApi(book.getIsbnThirteen())
        );

    }

    @Test
    public void addBookNotInAPI() {

        Book book = new Book(
                RandomValuesGenerator.generateNumeric(10),
                RandomValuesGenerator.generateNumeric(13),
                RandomValuesGenerator.generateAlphaNumeric(5),
                Book.Language.English);

        when(bookApi.getBook(any())).thenReturn(null);

        when(bookRepo.findByIsbnTen(anyString())).thenReturn(null);

        assertThrowsExactly(
                BookAdditionError.BookNotFound.class,
                () -> service.addBookFromApi(book.getIsbnTen())
        );

    }

    @Test
    public void searchOwnersOfNonExistentBook() {

        Book book = new Book(
                RandomValuesGenerator.generateNumeric(10),
                RandomValuesGenerator.generateNumeric(13),
                RandomValuesGenerator.generateAlphaNumeric(5),
                Book.Language.English);

        when(bookRepo.findByIsbnTen(book.getIsbnTen())).thenReturn(null);
        when(bookRepo.findByIsbnThirteen(book.getIsbnThirteen())).thenReturn(null);


        assertThrowsExactly(
                BookOwnersSearchError.BookNotFound.class,
                () -> service.getOwnersOfBook(book.getIsbnTen(), 0)
        );

        assertThrowsExactly(
                BookOwnersSearchError.BookNotFound.class,
                () -> service.getOwnersOfBook(book.getIsbnThirteen(), 0)
        );

    }


    @Test
    public void searchOwnersOfBook() throws BookOwnersSearchError {

        Book book = TestData.databaseBooks[0];
        int pageNumber = 0;

        List<User> owners = TestData.users;

        Page<Owned> page = new PageImpl<Owned>(owners.stream().map(user -> new Owned(user, book)).toList());

        when(bookRepo.findByIsbnTen(book.getIsbnTen())).thenReturn(book);


        when(ownedRepo.findByBookId(book.getId(), PageRequest.of(pageNumber, 20)))
                .thenReturn(page);

        assertEquals(owners, service.getOwnersOfBook(book.getIsbnTen(), pageNumber).toList());

    }

    @Test
    public void lendBookSuccessfully() {

        User lender = TestData.users.getFirst();

        Book book = TestData.databaseBooks[0];
        when(bookRepo.findByIsbnTen(book.getIsbnTen())).thenReturn(book);
        when(bookRepo.findByIsbnTen(book.getIsbnThirteen())).thenReturn(book);

        User receiver = TestData.users.get(1);
        when(userRepo.findByEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));

        Owned owned = new Owned(lender, book);

        when(ownedRepo.findById(owned.getId())).thenReturn(Optional.of(new Owned(lender, book)));

        when(requestRepo.findById(any())) //new RequestId(owned.getId(), receiver.getId()) needs to be any?
                .thenReturn(Optional.of(new Request(owned, receiver.getId(), 5)));

        when(lendRepo.existsById(owned.getId())).thenReturn(false);

        when(userService.checkAuthentication(anyString())).thenReturn(lender);

        try {
            service.lendBook(book.getIsbnTen(), receiver.getEmail(), "token");
        } catch (Exception e) {
            fail("Should not fail", e);
        }


    }

}