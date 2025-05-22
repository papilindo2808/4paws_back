package org.example._4paws_project.controllers;

import org.example._4paws_project.models.Post;
import org.example._4paws_project.models.User;
import org.example._4paws_project.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/community/{communityId}")
    public List<Post> getPostsByCommunity(@PathVariable Long communityId) {
        return postService.getPostsByCommunity(communityId);
    }

    @GetMapping("/community/{communityId}/recent")
    public List<Post> getPostsByCommunityOrderByDate(@PathVariable Long communityId) {
        return postService.getPostsByCommunityOrderByDate(communityId);
    }

    @GetMapping("/community/{communityId}/popular")
    public List<Post> getPostsByCommunityOrderByLikes(@PathVariable Long communityId) {
        return postService.getPostsByCommunityOrderByLikes(communityId);
    }

    @PostMapping
    public Post createPost(@RequestBody Post post, @AuthenticationPrincipal User user) {
        if (user != null) {
            post.setAuthor(user);
        } else {
            throw new RuntimeException("No se pudo determinar el autor de la publicación");
        }
        return postService.createPost(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        try {
            Post updatedPost = postService.updatePost(id, postDetails);
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Post> likePost(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            Post post = postService.likePost(id, user);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/unlike")
    public ResponseEntity<Post> unlikePost(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            Post post = postService.unlikePost(id, user);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}