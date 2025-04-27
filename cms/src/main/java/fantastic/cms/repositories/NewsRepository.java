package fantastic.cms.repositories;
import fantastic.cms.models.Category;
import fantastic.cms.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, String> {
    List<News> findByCategory(Category category);
    List<News> findByAuthorId(String author);
    void deleteByCategoryId(String categoryId);

    @Query("SELECT DISTINCT n FROM News n LEFT JOIN FETCH n.comments c LEFT JOIN FETCH c.author")
    List<News> findAllWithComments();
}
