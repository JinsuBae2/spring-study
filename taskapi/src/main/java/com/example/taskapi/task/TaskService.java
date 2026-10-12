package com.example.taskapi.task;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final JpaTaskRepository jpaTaskRepository;

    public TaskService(JpaTaskRepository jpaTaskRepository) {
        this.jpaTaskRepository = jpaTaskRepository;
    }

    @Transactional
    public Task createTask(String title, String description) {
        Task task = new Task(title, description);
        jpaTaskRepository.save(task);

        return task;
    }

    public List<Task> findAll() {
        return jpaTaskRepository.findAllByOrderByIdAsc();
    }

    public Task findById(int id) {
        return jpaTaskRepository.findById(id).orElse(null);
    }

    @Transactional
    public Task startTask(int id) {
        Task task = jpaTaskRepository.findById(id).orElse(null);

        if (task == null) {
            return null;
        }
        task.start();
        return task;
    }

    @Transactional
    public Task completeTask(int id) {
        Task task = jpaTaskRepository.findById(id).orElse(null);

        if (task == null) {
            return null;
        }

        task.complete();
        return task;
    }

    @Transactional
    public Task updateTask(int id, String title, String description) {
        Task task = jpaTaskRepository.findById(id).orElse(null);

        if (task == null) {
            return null;
        }

        task.update(title, description);

        return task;
    }

    @Transactional
    public boolean deleteTask(int id) {
        Task task = jpaTaskRepository.findById(id).orElse(null);

        if (task == null) {
            return false;
        }

        jpaTaskRepository.delete(task);

        return true;
    }
}
