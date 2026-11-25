package backend.bookSharing.data;

import static backend.bookSharing.repository.entities.Book.Language.English;
import static backend.bookSharing.repository.entities.Book.Language.French;

import backend.bookSharing.repository.entities.Book;

public class DatabaseBooks {
    public static final TestBook[] books = {
        new TestBook(
                "0345296052",
                "9780345296054",
                "The Fellowship of the Ring",
                English), // does not have language on openLibrary API, but only on json?
        // https://openlibrary.org/works/OL27513W/The_Fellowship_of_the_Ring?edition=key%3A/books/OL24373119M
        new TestBook("0553573403", "9780553573404", "A Game of Thrones", English),
        new TestBook(
                "9896419930",
                "9789896419936",
                "A Muralha",
                Book.Language.Portuguese), // had the book
        new TestBook("0910050686", "9780910050685", "Museum masters", English),
        new TestBook("8702347032", "9788702347036", "Eventyret om ringen", Book.Language.Danish),
        new TestBook("0316312487", "9780316312486", "One Dark Window", English),
        new TestBook("0810984911", "9780810984912", "The Ugly Truth", English),
        new TestBook("0141182474", "9780141182476", "Nineteen Eighty-Four", English),
        new TestBook("0517020785", "9780517020784", "The Natural House", English),
        new TestBook("1535299533", "9781535299534", "War And Peace", English),
        new TestBook("1444808249", "9781444808247", "The Case Book of Sherlock Holmes", English),
        new TestBook("140430732X", "9781404307322", "The History of Herodotus", English), //
        new TestBook("1542878160", "9781542878166", "The Great Gatsby", English),
        new TestBook("1600965229", "9781600965227", "The Man Who Knew Too Much", English),
        new TestBook("1440471045", "9781440471049", "Siddhartha", English),
        new TestBook("0671453521", "9780671453527", "Wuthering Heights", English),
        new TestBook("0671481193", "9780671481193", "Vanity Fair", English),
        new TestBook("0820703931", "9780820703930", "Paradise Lost", English),
        new TestBook("1582872554", "9781582872551", "The lost princess of Oz", English),
        new TestBook(null, "9798604654095", "Paradise Lost", English),
        new TestBook(
                "1566194326",
                "9781566194327",
                "Crime and Punishment (Barnes and Noble Classics)",
                English),
        // two languages
        new TestBook("1145233589", "9781145233584", "Le Comte de Monte-Cristo", French),
    };
}
