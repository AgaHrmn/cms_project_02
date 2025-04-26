package fantastic.cms.controllers;

import fantastic.cms.models.Category;
import fantastic.cms.models.News;
import fantastic.cms.models.User;
import fantastic.cms.requests.DeleteNewsRequest;
import fantastic.cms.requests.NewsRequest;
import fantastic.cms.services.CategoryService;
import fantastic.cms.services.NewsService;
import fantastic.cms.services.UserService;
import fantastic.cms.services.CommentService;
import fantastic.cms.models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

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

    @PostMapping(value = "/news/delete")
    public String deleteNews(@ModelAttribute DeleteNewsRequest deleteNewsRequest, RedirectAttributes redirectAttributes) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        try {
            newsService.delete(deleteNewsRequest.getNewsId(), currentPrincipalName);
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("errorDeleteNews", e.getMessage());
            return "redirect:/main";
        }
        return "redirect:/main";
    }

        @GetMapping("/news/{id}/comments")
    public String showComments(@PathVariable String id, Model model) {
        List<Comment> comments = commentService.findByNews(id);
        model.addAttribute("comments", comments);
        return "comments";
    }

    @PostMapping("/news/{id}/comments")
    public String addComment(@PathVariable String id,
                            @RequestParam String content,
                            Authentication authentication) {
        String username = authentication.getName();
        User author = userService.findByUsername(username);
        commentService.addComment(id, author.getId(), content);
        return "redirect:/news/" + id + "/comments";
    }

}
