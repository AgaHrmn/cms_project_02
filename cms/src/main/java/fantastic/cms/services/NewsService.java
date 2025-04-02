package fantastic.cms.services;

import fantastic.cms.repositories.NewsRepository;
import fantastic.cms.repositories.CategoryRepository;
import fantastic.cms.requests.NewsRequest;
import fantastic.cms.models.News;
import fantastic.cms.models.User;
import fantastic.cms.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService; // Added UserService dependency

    // Inject UserService into constructor along with other dependencies
    @Autowired
    public NewsService(NewsRepository newsRepository, CategoryRepository categoryRepository, UserService userService) {
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService; // Injecting UserService
    }

    @Transactional
    public News create(NewsRequest newsRequest) {
        News news = new News();
        news.setTitle(newsRequest.getTitle());
        news.setContent(newsRequest.getContent());

        // Ensure the category is not null (single category)
        if (newsRequest.getCategory() == null) {
            throw new RuntimeException("Category must be provided.");
        }

        // Find the category by ID or create it if it doesn't exist
        Category category = categoryRepository.findById(newsRequest.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + newsRequest.getCategory().getId()));

        news.setCategory(category); // Set the single category

        // Get the current authenticated user and set as author
        User currentUser = userService.getCurrentAuthenticatedUser(); // This will fetch the currently authenticated user
        if (currentUser != null) {
            news.setAuthor(currentUser); // Set author as the current user
        }

        return newsRepository.save(news); // Save the news to the repository
    }

    @Transactional
    public News update(String id, NewsRequest newsRequest) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

        if (newsRequest.getTitle() != null) {
            news.setTitle(newsRequest.getTitle());
        }
        if (newsRequest.getContent() != null) {
            news.setContent(newsRequest.getContent());
        }

        // Handle category update
        if (newsRequest.getCategory() != null) {
            Category category = categoryRepository.findById(newsRequest.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + newsRequest.getCategory().getId()));

            news.setCategory(category);
        }

        return newsRepository.save(news);
    }

    public void delete(String id) {
        if (!newsRepository.existsById(id)) {
            throw new RuntimeException("News with ID " + id + " not found.");
        }
        newsRepository.deleteById(id);
    }

    public List<News> findAll() {
        return newsRepository.findAll();
    }

    public Optional<News> findOne(String id) {
        return newsRepository.findById(id);
    }

    public List<News> findNewsByCategory(String categoryId) {
        // Find the Category by ID
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));

        // Fetch all News associated with the category
        return newsRepository.findByCategory(category); // Assuming findByCategory exists in your repository
    }
}
