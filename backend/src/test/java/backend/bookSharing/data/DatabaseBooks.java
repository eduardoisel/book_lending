package backend.bookSharing.data;

import static backend.bookSharing.repository.entities.Book.Language.English;
import static backend.bookSharing.repository.entities.Book.Language.French;

import backend.bookSharing.repository.entities.Book;

public class DatabaseBooks {
    public static final Book[] books = {
        new Book(
                "0345296052",
                "9780345296054",
                "The Fellowship of the Ring",
                English), // does not have language on openLibrary API, but only on json?
        // https://openlibrary.org/works/OL27513W/The_Fellowship_of_the_Ring?edition=key%3A/books/OL24373119M
        new Book("0553573403", null, "A Game of Thrones", English),
        new Book(null, "9789896419936", "A Muralha", Book.Language.Portuguese), // had the book
        new Book("0910050686", null, "Museum masters", English),
        new Book(null, "9788702347036", "Eventyret om ringen", Book.Language.Danish),
        new Book(null, "9780316312486", "One Dark Window", English),
        new Book("0810984911", null, "The Ugly Truth", English),
        new Book("0141182474", null, "Nineteen Eighty-Four", English),
        new Book("0517020785", "9780517020784", "The Natural House", English),
        new Book("1535299533", "9781535299534", "War And Peace", English),
        new Book("1444808249", "9781444808247", "The Case Book of Sherlock Holmes", English),
        new Book("140430732X", "9781404307322", "The History of Herodotus", English), //
        new Book("1542878160", "9781542878166", "The Great Gatsby", English),
        new Book("1600965229", "9781600965227", "The Man Who Knew Too Much", English),
        new Book("1440471045", "9781440471049", "Siddhartha", English),
        new Book("0671453521", null, "Wuthering Heights", English),
        new Book("0671481193", "9780671481193", "Vanity Fair", English),
        new Book("0820703931", "9780820703930", "Paradise Lost", English),
        new Book("1582872554", "9781582872551", "The lost princess of Oz", English),
        new Book(null, "9798604654095", "Paradise Lost", English),
        new Book("0820703931", "9780820703930", "Anne of Green Gables", English),
        new Book(
                "1566194326",
                "9781566194327",
                "Crime and Punishment (Barnes and Noble Classics)",
                English),
        new Book("1145233589", "9781145233584", "Le Comte de Monte-Cristo", French),
    };
}
