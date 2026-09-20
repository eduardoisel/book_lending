package backend.bookSharing.http.data;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

public record UserLogin(@JsonSetter(nulls = Nulls.FAIL) String email, @JsonSetter(nulls = Nulls.FAIL) String password) {
}
