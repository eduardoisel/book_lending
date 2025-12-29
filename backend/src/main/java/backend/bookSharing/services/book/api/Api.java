package backend.bookSharing.services.book.api;

import backend.bookSharing.repository.entities.Book;

public interface Api {

    /**
     *
     * @param isbn, HAS TO BE 10 or 13 digits, as these are the only acceptable isbn numbers
     *
     * @return Book information from the API
     */
    public Book getBook(String isbn);
}
