package com.example.taskapi.task;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {

    private int cnt;
    private final List<Task> tasks = new ArrayList<>();

    public Task save(Task task) {
        // task에 업무를 추가하고, 추가한 업무를 반환
        if (task != null) {
            cnt++;
            task.setId(cnt);
            tasks.add(task);
        }

        return task;
    }

    public List<Task> findAll() {
        return List.copyOf(tasks);
    }

    public Task findById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }
    public void deleteTask(Task task) {
        tasks.remove(task);
    }
}
