package backend.bookSharing.repository.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Book {

    public enum Language {
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
    @Column(unique = false, nullable = true, name = "isbn_10", length = 10)
    private String isbnTen;

    //not unique, can be null
    @Column(unique = false, nullable = true, name = "isbn_13", length = 13)
    private String isbnThirteen;

    @Column(length = 100, unique = false, nullable = false)
    private String title;

    //https://www.tutorialpedia.org/blog/how-to-map-postgresql-enum-with-jpa-and-hibernate/ to see
    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Language language;

    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY) //note: mapped by string value is from owned class Owned reference name field
    @BatchSize(size = 20)
    @ToString.Exclude
    private List<Owned> owners; //private Set<Owned> owners;

    public Book(String isbn_10, String isbn_13, String title, Language language){
        this.isbnTen = isbn_10;
        this.isbnThirteen = isbn_13;
        this.title = title;
        this.language = language;
    }

}
