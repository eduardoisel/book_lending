package backend.bookSharing.repository.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "Region")
@NoArgsConstructor
public class Region {

    @Id
    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private List<User> belongs;

    public Region(String name){
        this.name = name;
    }


    @Override
    public String toString() {
        return String.format("Region[name='%s']", name);
    }


}
