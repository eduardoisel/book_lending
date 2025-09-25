package backend.bookSharing.repository.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer isbn_10;

    private Long isbn_13;

    public Book(Integer isbn_10, Long isbn_13){
        this.isbn_10 = isbn_10;
        this.isbn_13 = isbn_13;
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
}
