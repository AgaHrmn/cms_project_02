package fantastic.cms.repositories;

import fantastic.cms.models.Comment;
import fantastic.cms.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByNews(News news);
}