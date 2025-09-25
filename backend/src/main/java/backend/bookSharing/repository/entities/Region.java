package backend.bookSharing.repository.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Region")
public class Region {

    @Id
    private String name;

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private List<User> belongs;

    public Region(String name){
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
