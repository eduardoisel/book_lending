package backend.bookSharing.services.book;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.book.api.BookApi;
import backend.bookSharing.services.book.failures.BookAdditionError;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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


    public void addBookFromApi(String isbn) throws  BookAdditionError{

        boolean isIsbn10 = isbn.length() == 10;

        if (isIsbn10 && repo.findByIsbnTen(isbn) != null){
                throw new BookAdditionError.Isbn10InUse();
        }

        if (!isIsbn10 && repo.findByIsbnThirteen(isbn) != null){
            throw new BookAdditionError.Isbn13InUse();
        }

        repo.save(bookApi.getBook(isbn));

    }


}
