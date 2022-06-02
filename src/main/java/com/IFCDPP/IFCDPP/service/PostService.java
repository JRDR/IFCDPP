package com.IFCDPP.IFCDPP.service;

import com.IFCDPP.IFCDPP.entity.PostEntity;
import com.IFCDPP.IFCDPP.models.Post;
import com.IFCDPP.IFCDPP.repo.PostRepository;
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
