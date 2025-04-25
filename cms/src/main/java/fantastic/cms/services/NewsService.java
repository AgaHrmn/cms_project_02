package fantastic.cms.services;

import fantastic.cms.constant.UserType;
import fantastic.cms.models.News;
import fantastic.cms.models.Category;
import fantastic.cms.models.User;
import fantastic.cms.repositories.NewsRepository;
import fantastic.cms.repositories.CategoryRepository;
import fantastic.cms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fantastic.cms.requests.NewsRequest;
import org.springframework.transaction.annotation.Transactional;
import fantastic.cms.services.UserService;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public List<News> findAll() {
        return newsRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public void create(NewsRequest newsRequest) {
        News news = new News();
        news.setTitle(newsRequest.getTitle());
        news.setContent(newsRequest.getContent());

        System.out.println("DEBUG - Category ID: " + newsRequest.getCategoryId());
        System.out.println("DEBUG - New Category Name: " + newsRequest.getNewCategoryName());

        // Handle category selection or creation
        Category category = null;

        if (newsRequest.getNewCategoryName() != null && !newsRequest.getNewCategoryName().trim().isEmpty()) {
            category = new Category();
            category.setName(newsRequest.getNewCategoryName().trim());
            category = categoryRepository.save(category); // Save new category
            System.out.println("DEBUG - Created new category: " + category.getName());
        } else if (newsRequest.getCategoryId() != null && !newsRequest.getCategoryId().isEmpty()) {
            category = categoryRepository.findById(newsRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            System.out.println("DEBUG - Using existing category: " + category.getName());
        }

        if (category == null) {
            System.out.println("ERROR - No category provided.");
            throw new RuntimeException("Category is required.");
        }

        news.setCategory(category);

        // Assign author
        User author = userRepository.findById(newsRequest.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        news.setAuthor(author);

        newsRepository.save(news);
    }

    @Transactional
    public void delete(String newsId, String currentPrincipalName) throws AccessDeniedException {
        News news = newsRepository.findById(newsId).orElseThrow();
        var type = userRepository.findByUsername(currentPrincipalName).getType();
        String currentPrincipalId = userRepository.findByUsername(currentPrincipalName).getId();
        if (type == UserType.ADMIN || type == UserType.MODERATOR) {
            newsRepository.delete(news);
        } else if (type == UserType.STANDARD_USER && news.author.getId().equals(currentPrincipalId)) {
            newsRepository.delete(news);
        } else {
            throw new AccessDeniedException("Użytkownik nie posiada uprawnień do usunięcia newsa");
        }
    }

}
