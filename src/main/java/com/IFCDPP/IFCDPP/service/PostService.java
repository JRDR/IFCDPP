package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.PostEntity;
import com.ifcdpp.ifcdpp.models.Post;
import com.ifcdpp.ifcdpp.repo.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getAllPosts() {
        List<PostEntity> entities = postRepository.findAll();
        return entities.stream().map(x -> new Post(x.getId(), x.getTitle(), x.getText(), x.getViews()))
                .collect(Collectors.toList());
    }

    public void savePost(String title, String text) {
        PostEntity post =new PostEntity(title, text);
        postRepository.save(post);
    }

}
