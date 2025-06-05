package exercise.controller.users;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {

    List<Post> posts = Data.getPosts();

    @GetMapping("/users/{id}/posts")
    ResponseEntity<List<Post>> findUserPosts(@PathVariable int id) {
        var result = posts.stream().filter(p -> p.getUserId() == id).toList();
        ResponseEntity<List<Post>> response = null;
        if (result.isEmpty()) {
            response = ResponseEntity.ok().body(Collections.emptyList());
        } else {
            response = ResponseEntity.ok().body(result);
        }
        return response;
    }

    @PostMapping("/users/{id}/posts")
    ResponseEntity<Post> addUserPost(@RequestBody Post post, @PathVariable int id) {
        post.setUserId(id);
        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
}
// END
