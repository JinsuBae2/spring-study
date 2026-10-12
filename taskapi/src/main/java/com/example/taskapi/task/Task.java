package com.example.taskapi.task;

import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    protected Task() {}

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

    public void update(String title, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
        }
        this.title = title;
        this.description = description;
    }
}
