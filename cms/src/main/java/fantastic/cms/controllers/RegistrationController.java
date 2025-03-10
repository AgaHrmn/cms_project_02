package fantastic.cms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import fantastic.cms.models.User;
import fantastic.cms.services.UserService;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("usernameError", "Username already taken");
            return "register";
        }
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("EmailError", "Email already taken");
            return "register";
        }

        try {
            userService.registerNewUser(user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        return "redirect:/login";
    }
}