package backend.bookSharing.repository.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Region")
public class Region {

    @Id
    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private List<User> belongs;

    public Region(){} //seems to be necessary for hibernate

    public Region(String name){
        this.name = name;
    }


    @Override
    public String toString() {
        return String.format("Region[name='%s']", name);
    }


    public String getName() {
        return name;
    }
}
