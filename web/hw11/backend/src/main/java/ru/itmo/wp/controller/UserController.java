package ru.itmo.wp.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.form.RegisterUserCredentials;
import ru.itmo.wp.service.JwtService;
import ru.itmo.wp.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1")
public class UserController {
    private final JwtService jwtService;
    private final UserService userService;

    public UserController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("users")
    public List<User> findPosts() {
        return userService.findAll();
    }

    @GetMapping("users/findByLogin/{login}")
    public boolean findByLogin(@PathVariable String login) {
        return userService.findByLogin(login);
    }

    @PostMapping("users")
    public void register(@Valid @RequestBody RegisterUserCredentials userCredentials, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            userService.register(userCredentials);
        }
    }

    @GetMapping("users/auth")
    public User findUserByJwt(@RequestParam String jwt) {
        return jwtService.find(jwt);
    }

}
