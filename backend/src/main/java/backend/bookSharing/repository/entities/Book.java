package backend.bookSharing.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
//import org.hibernate.dialect.PostgreSQLEnumJdbcType;

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
        Hebrew,
        Hungarian,
        Danish
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //not unique, can be null
    @Column(unique = false, nullable = true, name = "isbn_10")
    private Integer isbnTen;

    //not unique, can be null
    @Column(unique = false, nullable = true, name = "isbn_13")
    private Long isbnThirteen;

    @Column(length = 100, unique = false, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Language language;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY) //note: mapped by string value is from owned class Owned reference name field
    @BatchSize(size = 20)
    private List<Owned> owners; //private Set<Owned> owners;

    public Book(){} //seems to be necessary for hibernate

    public Book(Integer isbn_10, Long isbn_13, String title, Language language){
        this.isbnTen = isbn_10;
        this.isbnThirteen = isbn_13;
        this.title = title;
        this.language = language;
    }

    @Override
    public String toString() {
        return String.format(
                "Book[id='%d', isbn_10='%d', isbn_13='%d', title='%s', language='%s']",
                id, isbnTen, isbnThirteen, title, language.toString());
    }

    public Integer getId() {
        return id;
    }

    public Integer getIsbnTen() {
        return isbnTen;
    }

    public Long getIsbnThirteen() {
        return isbnThirteen;
    }

    public String getTitle() {
        return title;
    }

    public Language getLanguage() {
        return language;
    }

    public List<Owned> getOwners() {
        return owners;
    }

}
