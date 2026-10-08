package com.example.taskapi.task;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("/sample")
    public Task getSampleTask() {
        // 지정된 제목과 설명으로 Task 반환
        return new Task("스프링 학습", "첫 조회 API 구현");
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody TaskCreateRequest request) {
        return taskService.createTask(request.title(), request.description());
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable int id) {
        Task task = taskService.findById(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<Task> startTask(@PathVariable int id) {
        Task task = taskService.findById(id);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        taskService.startTask(task);

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable int id) {
        Task task = taskService.findById(id);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        taskService.completeTask(task);

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @Valid @RequestBody TaskUpdateRequest request) {
        Task task = taskService.findById(id);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        taskService.updateTask(task, request.title(), request.description());

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        Task task = taskService.findById(id);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        taskService.deleteTask(task);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
