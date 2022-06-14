package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.PostEntity;
import com.ifcdpp.ifcdpp.exceptions.MessageException;
import com.ifcdpp.ifcdpp.models.Blog;
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

    public Blog getPostsOnPage(Integer page) {
        if (page == null) {
            page = 1;
        }
        Pageable pageWithFiveElems = PageRequest.of(page - 1, 5);
        Page<PostEntity> entities = postRepository.findAll(pageWithFiveElems);
        int pagesNum = entities.getTotalPages();
        List<Post> posts = entities.stream().map(this::mapPost).collect(Collectors.toList());

        return new Blog(posts, pagesNum);
    }

    public Long savePost(Post post) {
        PostEntity entity;
        if (post.getId() != null) {
            entity = postRepository.findById(post.getId()).orElseThrow(() -> new MessageException("Post not found"));
            entity.setTitle(post.getTitle());
            entity.setText(post.getText());
        } else {
            entity = new PostEntity(post.getTitle(), post.getText());
        }
        PostEntity newEntity = postRepository.save(entity);
        return newEntity.getId();
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public Post getPostById(Long id) {
        Optional<PostEntity> entity = postRepository.findById(id);
        return entity.map(this::mapPost).orElse(null);
    }

    private Post mapPost(PostEntity entity) {
        return Post.builder().id(entity.getId()).title(entity.getTitle()).text(entity.getText()).build();
    }

}
