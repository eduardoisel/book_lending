package backend.bookSharing.repository.entities.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
public class Book {

    public static enum Language {
        English,
        Portuguese,
        Spanish,
        French,
        German,
        Russian,
        Japanese,
        Italian,
        Hebrew;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = false, nullable = true)
    private Integer isbn_10;

    @Column(unique = false, nullable = true)
    private Long isbn_13;

    @Column(length = 100, unique = false, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private Language language;

    public Book() {}

    public Book(Integer isbn_10, Long isbn_13, String title, Language language){
        this.isbn_10 = isbn_10;
        this.isbn_13 = isbn_13;
        this.title = title;
        this.language = language;
    }

    @Override
    public String toString() {
        return String.format(
                "Book[id='%d', isbn_10='%d', isbn_13='%d', title='%s', language='%s']",
                id, isbn_10, isbn_13, title, language.toString());
    }

    public Integer getId() {
        return id;
    }

    public Integer getIsbn_10() {
        return isbn_10;
    }

    public Long getIsbn_13() {
        return isbn_13;
    }

    public String getTitle() {
        return title;
    }

    public Language getLanguage() {
        return language;
    }
}
