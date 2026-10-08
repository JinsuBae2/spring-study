package com.example.taskapi.task;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @GetMapping("/sample")
    public Task getSampleTask() {
        // 지정된 제목과 설명으로 Task 반환
        return new Task("스프링 학습", "첫 조회 API 구현");
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody TaskCreateRequest request) {
        // request의 제목과 설명으로 Task를 생성해서 반환하세요.
        Task task = new Task(request.title(), request.description());
        taskRepository.save(task);
        return task;
    }

    @GetMapping
    public List<Task> getTasks() {
        // Repository에서 목록 조회해서 반환
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable int id) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<Task> startTask(@PathVariable int id) {
        Task task = taskRepository.findById(id);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (task.getStatus() != TaskStatus.TODO) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        task.start();

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable int id) {
        Task task = taskRepository.findById(id);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (task.getStatus() != TaskStatus.IN_PROGRESS) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        task.complete();

        return ResponseEntity.ok(task);
    }


}
