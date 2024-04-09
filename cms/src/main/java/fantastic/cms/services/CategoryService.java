package fantastic.cms.services;

import fantastic.cms.requests.CategoryRequest;
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

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category update(Category category) {
        return this.categoryRepository.save(category);
    }

    @Transactional
    public Category create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        return this.categoryRepository.save(category);
    }

    @Transactional
    public void delete(String id) {
        this.categoryRepository.deleteById(id);
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
