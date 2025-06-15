package exercise.controller;

import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post with id " + id + " not found")
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post) {
        postRepository.save(post);
        return post;
    }

    @PutMapping("/{id}")
    public Post updatePost(@RequestBody Post post, @PathVariable Long id) {
        var postToUpdate = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post with id " + id + " not found")
        );
        postToUpdate.setBody(post.getBody());
        postToUpdate.setTitle(post.getTitle());
        postRepository.save(postToUpdate);
        return postToUpdate;
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        var postFromDb = postRepository.findById(id);
        Long postId = null;
        if (postFromDb.isPresent()) {
            postId = postFromDb.get().getId();
        } else {
            throw new ResourceNotFoundException("Post with id " + id + " not found");
        }
        commentRepository.deleteByPostId(postId);
        postRepository.deleteById(id);
    }
}
// END
