package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class PostPage extends Page {
    private final PostService postService;

    public PostPage(PostService postService) {
        this.postService = postService;
    }

    @GetMapping({"/post/{id}", "/post"})
    @Guest
    public String getPost(@PathVariable(required = false) String id, Model model) {
        try {
            Post post = postService.findPostById(Long.parseLong(id));
            if (post != null) {
                model.addAttribute(post);
                if (model.getAttribute("comment") == null) {
                    model.addAttribute("comment", new Comment());
                }
            }

        } catch (NumberFormatException e) {
            // no operations
        }
        return "PostPage";
    }

    @PostMapping("/post/{id}")
    public String commentPost(@PathVariable String id,
                              @Valid @ModelAttribute("comment") Comment comment,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model
    ) {
        if (bindingResult.hasErrors()) {
//            return "PostPage";
            return getPost(id, model);
//            putMessage(session, "bad comment");
//            return "redirect:/post/" + id;
        }
        try {
            Post post = postService.findPostById(Long.parseLong(id));
            postService.writeComment(post, getUser(session), comment);
        } catch (NumberFormatException e) {
            // no operations
        }
        return "redirect:/post/" + id;
    }
}
