package com.example.taskapi.task;

import org.jspecify.annotations.NonNull;

public class Task {
    private int id;
    private String title;
    private String description;
    private TaskStatus status;

    public Task(@NonNull String title, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
        }
        this.title = title;
        this.description = description;
        this.status = TaskStatus.TODO;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void start() throws IllegalStateException {
        // TODO 상태에서만 시작할 수 있습니다.
        if (this.status != TaskStatus.TODO) {
            throw new IllegalStateException("TODO 상태에서만 시작할 수 있습니다.");
        }
        this.status = TaskStatus.IN_PROGRESS;
    }

    public void complete() throws IllegalStateException {
        // IN_PROGRESS 상태에서만 완할 수 있습니다.
        if (this.status != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException("IN_PROGRESS 상태에서만 시작할 수 있습니다.");
        }
        this.status = TaskStatus.DONE;
    }
}
