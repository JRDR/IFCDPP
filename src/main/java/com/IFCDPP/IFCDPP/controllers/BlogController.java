package com.IFCDPP.IFCDPP.controllers;

import com.IFCDPP.IFCDPP.models.Post;
import com.IFCDPP.IFCDPP.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final PostService postService;

    @GetMapping("/blog")
    public String blogMain(Model model){
    List<Post> posts = postService.getAllPosts();
    model.addAttribute("posts",posts);
    return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model){
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String text){
        postService.savePost(title, text);
        return "redirect:/blog";
    }
}
