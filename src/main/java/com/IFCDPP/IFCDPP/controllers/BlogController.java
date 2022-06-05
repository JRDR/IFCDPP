package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.models.Post;
import com.ifcdpp.ifcdpp.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final PostService postService;

    @GetMapping("/blog")
    public String blogMain(@RequestParam(required = false) Integer page, Model model){
    List<Post> posts = postService.getPostsOnPage(page);
    model.addAttribute("posts",posts);
    return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model){
        return "blog-add";
    }

    @GetMapping("/blog/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "post";
    }

    @PostMapping(path = "/blog/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String blogPostAdd(Post post){
        postService.savePost(post);
        return "redirect:/blog";
    }
}
