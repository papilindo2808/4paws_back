package org.example._4paws_project.controllers;

import org.example._4paws_project.models.Comment;
import org.example._4paws_project.models.User;
import org.example._4paws_project.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/post/{postId}")
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    @GetMapping("/post/{postId}/recent")
    public List<Comment> getCommentsByPostOrderByDate(@PathVariable Long postId) {
        return commentService.getCommentsByPostOrderByDate(postId);
    }

    @PostMapping
    public Comment createComment(@RequestBody Comment comment, @AuthenticationPrincipal User user) {
        comment.setAuthor(user);
        return commentService.createComment(comment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment commentDetails) {
        try {
            Comment updatedComment = commentService.updateComment(id, commentDetails);
            return ResponseEntity.ok(updatedComment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Comment> likeComment(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            Comment comment = commentService.likeComment(id, user);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/unlike")
    public ResponseEntity<Comment> unlikeComment(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            Comment comment = commentService.unlikeComment(id, user);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 