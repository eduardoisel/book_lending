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
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Since this service is about physical books, it assumes the existence of the ISBN. It may have ISBN 10 and has
 * ISBN 13.
 * Should not be exposed to outside
 */
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
        Danish;

        /**
         * @param lang three character string from ISO_639
         * @return Enum representing Language
         * @see <a href="https://en.wikipedia.org/wiki/List_of_ISO_639_language_codes"></a>
         */
        public static Book.Language isoIdToLanguage(String lang) {
            return switch (lang) {
                case "eng" -> Book.Language.English;
                case "por" -> Book.Language.Portuguese;
                case "spa" -> Book.Language.Spanish;
                case "rus" -> Book.Language.Russian;
                case "jpn" -> Book.Language.Japanese;
                case "ger" -> Book.Language.German;
                case "heb" -> Book.Language.Hebrew;
                case "fra", "fre" -> Book.Language.French;
                case "ita" -> Book.Language.Italian;
                case "hun" -> Book.Language.Hungarian;
                case "dan" -> Book.Language.Danish;
                default ->
                        throw new RuntimeException(
                                "Language code %s from external API open library not supported"
                                        .formatted(lang));
            };
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^([0-9]{13})$")
    @Column(unique = true, nullable = false, name = "isbn_13", length = 13)
    private String isbnThirteen;

    @Column(length = 100, unique = false, nullable = false)
    private String title;

    // https://www.tutorialpedia.org/blog/how-to-map-postgresql-enum-with-jpa-and-hibernate/ to see
    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Language language;

    @OneToMany(
            mappedBy = "book",
            fetch = FetchType.LAZY) // note: mapped by string value is from owned class Owned
    // reference name field
    @BatchSize(size = 20)
    @ToString.Exclude
    private List<Owned> owners; // private Set<Owned> owners;

    public Book(String isbn_13, String title, Language language) {
        this.isbnThirteen = isbn_13;
        this.title = title;
        this.language = language;
    }
}
