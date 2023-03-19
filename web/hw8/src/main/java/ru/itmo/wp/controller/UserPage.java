package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.wp.service.UserService;

@Controller
@Validated
public class UserPage extends Page {
    private final UserService userService;

    public UserPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/user", "/user/{id}"})
    public String getUser(@PathVariable(value = "id", required = false) String id, Model model) {
        try {
            model.addAttribute("showUser", userService.findById(Long.parseLong(id)));
        } catch (NumberFormatException e) {
            // no operations
        }
        return "UserPage";
    }
}
