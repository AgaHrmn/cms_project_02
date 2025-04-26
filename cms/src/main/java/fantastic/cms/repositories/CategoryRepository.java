package fantastic.cms.repositories;

import java.util.List;
import fantastic.cms.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findByName(String name);
    List<Category>findByNameIgnoreCaseStartingWith(String name);
    List<Category>findByAuthorId(String author);
}
