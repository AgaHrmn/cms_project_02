package fantastic.cms.controllers;

//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class MainController {
//
//    @GetMapping("/")
//    public String main() {
//        return "main";
//    }
//}


import fantastic.cms.models.News;
import fantastic.cms.models.Category;
import fantastic.cms.models.User;
import fantastic.cms.services.NewsService;
import fantastic.cms.services.CategoryService;
import fantastic.cms.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.jaas.SecurityContextLoginModule;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MainController {

    private final NewsService newsService;
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public MainController(NewsService newsService, CategoryService categoryService, UserService userService) {
        this.newsService = newsService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String main(Model model) {
        // Fetch some example data from services
        List<News> newsList = newsService.findAll();
        List<Category> categoryList = categoryService.findAll();
        List<User> userList = userService.findAll();

        // Add data to the model
        model.addAttribute("newsList", newsList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("userList", userList);

        return "main";
    }
}

