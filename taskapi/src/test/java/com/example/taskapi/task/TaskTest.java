package com.example.taskapi.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskTest {

    @Test
    void 새_업무의_상태는_TODO이다() {
        Task task = new Task("테스트 공부", "테스트");

        assertEquals(TaskStatus.TODO, task.getStatus());
    }

    @Test
    void 빈_제목으로_업무_생성시_실패() {
        assertThrows(IllegalArgumentException.class, () -> new Task("", "설명"));
    }

    @Test
    void 업무를_시작하면_IN_PROGRESS가_된다() {
        Task task = new Task("상태 변경 구현", "");

        task.start();


        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    void 이미_시작한_업무를_다시_시작할_수_없다() throws IllegalAccessException {
        Task task = new Task("상태 변경 구현", "");
        task.start();

        assertThrows(IllegalStateException.class, () -> task.start());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }
}
