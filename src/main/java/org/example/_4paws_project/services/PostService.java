package org.example._4paws_project.services;

import org.example._4paws_project.models.Post;
import org.example._4paws_project.models.User;
import org.example._4paws_project.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getPostsByCommunity(Long communityId) {
        return postRepository.findByCommunityId(communityId);
    }

    public List<Post> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    public List<Post> getPostsByCommunityOrderByDate(Long communityId) {
        return postRepository.findByCommunityIdOrderByCreatedAtDesc(communityId);
    }

    public List<Post> getPostsByCommunityOrderByLikes(Long communityId) {
        return postRepository.findByCommunityIdOrderByLikesDesc(communityId);
    }

    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, Post postDetails) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setImageUrl(postDetails.getImageUrl());
        
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public Post likePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        
        if (!post.getLikedBy().contains(user)) {
            post.getLikedBy().add(user);
            post.setLikes(post.getLikes() + 1);
        }
        
        return postRepository.save(post);
    }

    @Transactional
    public Post unlikePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        
        if (post.getLikedBy().contains(user)) {
            post.getLikedBy().remove(user);
            post.setLikes(post.getLikes() - 1);
        }
        
        return postRepository.save(post);
    }
} 