package fantastic.cms.controllers;

import fantastic.cms.models.Category;
import fantastic.cms.requests.NewsRequest;
import fantastic.cms.services.CategoryService;
import fantastic.cms.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private CategoryService categoryService;

    // Display news creation form
    @GetMapping("/news/create")
    public String showNewsForm(Model model) {
        List<Category> categories = categoryService.findAll(); // Fetch all categories
        model.addAttribute("newsRequest", new NewsRequest());
        model.addAttribute("categories", categories); // Pass categories to the form
        return "newsForm"; // Return the name of the view (newsForm.html)
    }

    // Handle form submission
    @PostMapping("/news/create")
    public String createNews(@ModelAttribute NewsRequest newsRequest) {
        newsService.create(newsRequest);  // Call the service to save the news
        return "redirect:/main";  // Redirect to the main page or another page after submission
    }
}
