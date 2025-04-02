package fantastic.cms.repositories;
import fantastic.cms.models.Category;
import fantastic.cms.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, String> {
    List<News> findByCategory(Category category);
}
