package com.IFCDPP.IFCDPP.controllers;

import com.IFCDPP.IFCDPP.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.IFCDPP.IFCDPP.repo.PostRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMain(Model model){
    Iterable<Post> posts = postRepository.findAll();
    model.addAttribute("posts",posts);
    return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model){
        return "blog-add";
    }

    @PostMapping("/blod/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String text){
        Post post =new Post(title, text);
        postRepository.save(post);
        return "redirect:/blog";
    }
}
