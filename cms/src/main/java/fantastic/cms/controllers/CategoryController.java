package fantastic.cms.controllers;

import fantastic.cms.models.Category;
import fantastic.cms.models.News;
import fantastic.cms.models.User;
import fantastic.cms.requests.CategoryRequest;
import fantastic.cms.requests.DeleteCategoryRequest;
import fantastic.cms.services.CategoryService;
import fantastic.cms.services.NewsService;
import fantastic.cms.repositories.CategoryRepository;
import fantastic.cms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private NewsService newsService;

    @Autowired
    private CategoryRepository categoryRepository;  // Inject the CategoryRepository

    @Autowired
    private UserRepository userRepository;  // Inject UserRepository to get the list of users

    // Display the category creation form
    @GetMapping("/category/create")
    public String showCategoryForm(Model model) {
        model.addAttribute("categoryRequest", new CategoryRequest());
        return "categoryForm";
    }

    // Handle form submission and redirect to main.html
    @PostMapping("/category/create")
    public String saveCategory(@ModelAttribute CategoryRequest categoryRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        categoryService.create(categoryRequest, currentPrincipalName);  // Pass CategoryRequest to service
        return "redirect:/main";  // Redirect to main.html after category is created
    }

    // Display main page with updated list of categories and users
    @GetMapping("/main")
    public String showMainPage(Model model) {
        List<Category> categories = categoryRepository.findAll();  // Fetch all categories
        List<User> users = userRepository.findAll();  // Fetch all users
        model.addAttribute("categoryList", categories);  // Pass the category list to the template
        model.addAttribute("userList", users);  // Pass the user list to the template
        model.addAttribute("newsList", getNewsList());  // Add any news list if needed
        return "main";  // Return the view for the main page
    }

    @PostMapping(value = "/category/delete")
    public String deleteCategory(@ModelAttribute DeleteCategoryRequest deleteCategoryRequest, RedirectAttributes redirectAttributes) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        try {
            categoryService.delete(deleteCategoryRequest.getCategoryId(), currentPrincipalName);
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("errorDeleteCategory", e.getMessage());
            return "redirect:/main";
        }
        return "redirect:/main";  // Redirect to main.html after category is created
    }

    // Add logic to fetch news, if necessary
    private List<News> getNewsList() {
        // Example of how you could fetch news (if applicable)
        // This might come from a repository, service, or static data
        return newsService.findAll();
    }
}
