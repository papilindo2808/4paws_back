package org.example._4paws_project.services;

import org.example._4paws_project.models.Comment;
import org.example._4paws_project.models.User;
import org.example._4paws_project.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> getCommentsByAuthor(Long authorId) {
        return commentRepository.findByAuthorId(authorId);
    }

    public List<Comment> getCommentsByPostOrderByDate(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
    }

    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        
        comment.setContent(commentDetails.getContent());
        
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public Comment likeComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        
        if (!comment.getLikedBy().contains(user)) {
            comment.getLikedBy().add(user);
            comment.setLikes(comment.getLikes() + 1);
        }
        
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment unlikeComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        
        if (comment.getLikedBy().contains(user)) {
            comment.getLikedBy().remove(user);
            comment.setLikes(comment.getLikes() - 1);
        }
        
        return commentRepository.save(comment);
    }
} 