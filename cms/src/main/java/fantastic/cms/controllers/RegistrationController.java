package fantastic.cms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import fantastic.cms.models.User;
import fantastic.cms.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class RegistrationController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());  // Adds a new user object to the model for binding
        return "register";  // The form for registering a new user
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Check if username already exists
        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("usernameError", "Username is already taken.");
            return "register";  // Return to registration form if username exists
        }

        // Check if email already exists
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("emailError", "Email is already taken.");
            return "register";  // Return to registration form if email exists
        }

        // Try to register the user, catching potential errors
        try {
            userService.registerNewUser(user);  // Register the new user
            model.addAttribute("successMessage", "Registration successful! Please log in.");
            return "redirect:/login";  // Redirect to login page on successful registration
        } catch (Exception e) {
            logger.error("Error occurred during registration for user: {}", user.getUsername(), e);
            model.addAttribute("errorMessage", "An error occurred during registration. Please try again.");
            return "register";  // Return to registration form with error message
        }
    }
}
