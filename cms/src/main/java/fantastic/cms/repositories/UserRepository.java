package fantastic.cms.repositories;
import fantastic.cms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findByUsername(String username);
}
