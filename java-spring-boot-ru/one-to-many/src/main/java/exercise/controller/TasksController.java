package exercise.controller;

import java.util.ArrayList;
import java.util.List;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    // BEGIN
    @GetMapping
    public List<TaskDTO> getAllTasks() {
        List<TaskDTO> result = new ArrayList<>();
        taskRepository.findAll().forEach(
                taskEntity -> result.add(taskMapper.map(taskEntity))
        );
        return result;
    }

    @GetMapping("/{id}")
    public TaskDTO getTask(@PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with " + id + " not found"));
        return taskMapper.map(task);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO createTask(@Valid @RequestBody TaskCreateDTO task) {
        var taskEntity = taskRepository.save(taskMapper.map(task));
        return taskMapper.map(taskEntity);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@Valid @RequestBody TaskUpdateDTO task, @PathVariable Long id) {
        var taskFromDb = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskMapper.update(task, taskFromDb);
        var user = userRepository.findById(task.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id " + task.getAssigneeId() + " not found"
                ));
        user.assignTask(taskFromDb);
        taskRepository.save(taskFromDb);
        userRepository.save(user);
        return taskMapper.map(taskFromDb);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskRepository.delete(task);
        var userId = task.getAssignee().getId();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id " + userId + " not found"
                ));
        userRepository.save(user);
    }
    // END
}
