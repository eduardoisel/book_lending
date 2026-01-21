package backend.bookSharing.services.book;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.book.api.BookApi;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository repo;

    private final BookApi bookApi;

    public long bookCount(){
        return repo.count();
    }

    public List<User> getOwnersOfBook(Integer bookId){

        repo.findAll(PageRequest.of(1, 20)).getSort();
        return repo.getReferenceById(bookId).getOwners().stream().map(Owned::getUser).toList();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Optional<BookAdditionError> addBookFromApi(String isbn){

        boolean isIsbn10 = isbn.length() == 10;

        if (isIsbn10 && repo.findByIsbnTen(isbn) != null){
                return Optional.of(new BookAdditionError.Isbn10InUse());
        }

        if (!isIsbn10 && repo.findByIsbnThirteen(isbn) != null){
            return Optional.of(new BookAdditionError.Isbn13InUse());
        }

        repo.save(bookApi.getBook(isbn));

        return Optional.empty();

    }


}
