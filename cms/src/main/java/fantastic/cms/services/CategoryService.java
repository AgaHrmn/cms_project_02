package fantastic.cms.services;

import fantastic.cms.constant.UserType;
import fantastic.cms.repositories.NewsRepository;
import fantastic.cms.repositories.UserRepository;
import fantastic.cms.requests.CategoryRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import fantastic.cms.repositories.CategoryRepository;
import fantastic.cms.models.Category;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository, NewsRepository newsRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
    }

    @Transactional
    public Category update(Category category) {
        return this.categoryRepository.save(category);
    }

    @Transactional
    public Category create(CategoryRequest request, String userName) {
        Category category = new Category();
        category.setName(request.getName());
        category.author = userRepository.findByUsername(userName);
        return this.categoryRepository.save(category);
    }

    @Transactional
    public void delete(String categoryId, String currentPrincipalName) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        var type = userRepository.findByUsername(currentPrincipalName).getType();
        String currentPrincipalId = userRepository.findByUsername(currentPrincipalName).getId();
        boolean isAdminOrModerator = type == UserType.ADMIN || type == UserType.MODERATOR;
        boolean isAuthor = type == UserType.STANDARD_USER && category.author != null && category.author.getId().equals(currentPrincipalId);
        if (isAdminOrModerator || isAuthor) {
            newsRepository.deleteByCategoryId(categoryId);
            categoryRepository.delete(category);
        } else {
            throw new AccessDeniedException("Użytkownik nie posiada uprawnień do usunięcia kategorii");
        }
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    public List<Category> findByName(String name) {
        return this.categoryRepository.findByName(name);
    }

    public List<Category> findByNameStartingWith(String name) {
        return this.categoryRepository.findByNameIgnoreCaseStartingWith(name);
    }

    public Category findOne(String id) throws InstanceNotFoundException {
        final Optional<Category> category =
                this.categoryRepository.findById(id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new InstanceNotFoundException(id);
        }
    }
}
