package fantastic.cms.controllers;

import fantastic.cms.models.Category;
import fantastic.cms.models.News;
import fantastic.cms.models.User;
import fantastic.cms.requests.NewsRequest;
import fantastic.cms.services.CategoryService;
import fantastic.cms.services.NewsService;
import fantastic.cms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/news/create")
    public String showNewsForm(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "newsForm";
    }

    @PostMapping("/news/create")
    public String create(@RequestParam String title,
                         @RequestParam String content,
                         @RequestParam(required = false) String categoryId,
                         @RequestParam(required = false) String newCategoryName,
                         @RequestParam(required = false) boolean commentsEnabled) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User author = userService.findByUsername(username);

        if (author == null) {
            throw new RuntimeException("User not found");
        }

        System.out.println("DEBUG - Author ID: " + author.getId());
        System.out.println("DEBUG - Selected Category ID: " + categoryId);
        System.out.println("DEBUG - New Category Name: " + newCategoryName);
        System.out.println("DEBUG - comments toggle: " + commentsEnabled);

        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setTitle(title);
        newsRequest.setContent(content);
        newsRequest.setCategoryId(categoryId);
        newsRequest.setNewCategoryName(newCategoryName);
        newsRequest.setAuthorId(author.getId());
        newsRequest.setComments(commentsEnabled);
        newsService.create(newsRequest);

        return "redirect:/";
    }

}
