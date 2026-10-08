package com.example.taskapi.task;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(String title, String description) {
        Task task = new Task(title, description);
        taskRepository.save(task);

        return task;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(int id) {
        return taskRepository.findById(id);
    }

    public void startTask(Task task) {
        task.start();
    }

    public void completeTask(Task task) {
        task.complete();
    }

    public void updateTask(Task task, String title, String description) {
        task.update(title, description);
    }

    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    }
}
