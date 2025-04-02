package fantastic.cms.services;

import fantastic.cms.models.News;
import fantastic.cms.models.Category;
import fantastic.cms.models.User;
import fantastic.cms.repositories.NewsRepository;
import fantastic.cms.repositories.CategoryRepository;
import fantastic.cms.repositories.UserRepository;
import fantastic.cms.requests.NewsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;  // Inject CategoryRepository
    private final UserRepository userRepository;          // Inject UserRepository

    public NewsService(NewsRepository newsRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public News create(NewsRequest newsRequest) {
        Category category;

        if (newsRequest.getNewCategory() != null && !newsRequest.getNewCategory().isEmpty()) {
            // Create new category if it's provided in the request
            category = new Category();
            category.setName(newsRequest.getNewCategory());  // Set the name of the new category
            category = categoryRepository.save(category);   // Save the new category
        } else {
            // Fetch the Category by ID if no new category is provided
            category = categoryRepository.findById(newsRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        // Fetch the User (author) by ID
        User author = userRepository.findById(newsRequest.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        // Create a new News entity
        News news = new News();
        news.setTitle(newsRequest.getTitle());
        news.setContent(newsRequest.getContent());
        news.setCategory(category);  // Set the category
        news.setAuthor(author);      // Set the author

        return this.newsRepository.save(news);
    }

    public void delete(String id) {
        this.newsRepository.deleteById(id);
    }

    public List<News> findAll() {
        return this.newsRepository.findAll();
    }

    public News findOne(String id) {
        return this.newsRepository.findById(id).orElseThrow();
    }

}
