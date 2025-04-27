package fantastic.cms.services;

import fantastic.cms.models.Comment;
import fantastic.cms.models.News;
import fantastic.cms.models.User;
import fantastic.cms.repositories.CommentRepository;
import fantastic.cms.repositories.NewsRepository;
import fantastic.cms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Comment> findByNews(String newsId) {
        News news = newsRepository.findById(newsId).orElseThrow(() -> new RuntimeException("News not found"));
        return commentRepository.findByNews(news);
    }

    public void addComment(String newsId, String authorId, String content) {
        News news = newsRepository.findById(newsId).orElseThrow(() -> new RuntimeException("News not found"));
        User author = userRepository.findById(authorId).orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setNews(news);
        comment.setAuthor(author);
        comment.setContent(content);

        commentRepository.save(comment);
    }
}