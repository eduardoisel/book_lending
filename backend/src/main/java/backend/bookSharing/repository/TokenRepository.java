package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String>{
}
