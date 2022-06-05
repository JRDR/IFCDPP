package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.PostEntity;
import com.ifcdpp.ifcdpp.models.Post;
import com.ifcdpp.ifcdpp.repo.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll().stream().map(this::mapPost).collect(Collectors.toList());
    }

    public List<Post> getPostsOnPage(Integer page) {
        if (page == null) {
            page = 1;
        }
        Pageable pageWithFiveElems = PageRequest.of(page - 1, 5);
        Page<PostEntity> entities = postRepository.findAll(pageWithFiveElems);
        return entities.stream().map(this::mapPost).collect(Collectors.toList());
    }

    public void savePost(String title, String text) {
        PostEntity post = new PostEntity(title, text);
        postRepository.save(post);
    }

    public Post getPostById(Long id) {
        Optional<PostEntity> entity = postRepository.findById(id);
        return entity.map(this::mapPost).orElse(null);
    }

    private Post mapPost(PostEntity entity) {
        return Post.builder().id(entity.getId()).title(entity.getTitle()).text(entity.getText()).build();
    }

}
