package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment with id " + id + " not found")
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@RequestBody Comment comment) {
        commentRepository.save(comment);
        return comment;
    }

    @PutMapping("/{id}")
    public Comment updateComment(@RequestBody Comment comment, @PathVariable Long id) {
        var commentToUpdate = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment with id " + id + " not found")
        );
        commentToUpdate.setBody(comment.getBody());
        return commentToUpdate;
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
    }
}
// END
