package fantastic.cms.controllers;

import fantastic.cms.requests.DeleteUserRequest;
import fantastic.cms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/user/delete")
    public String deleteUser(@ModelAttribute DeleteUserRequest deleteUserRequest, RedirectAttributes redirectAttributes) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        try {
            userService.delete(deleteUserRequest.getUserId(), currentPrincipalName);
        } catch (org.springframework.security.access.AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("errorDeleteUser", e.getMessage());
            return "redirect:/main";
        }
        return "redirect:/home";
    }

}
