package com.example.taskapi.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService service;

    @BeforeEach
    void setUp() {
        TaskRepository repository = new TaskRepository();
        service = new TaskService(repository);
    }

    @Test
    void 업무를_생성하면_ID가_부여되고_저장된다() {
        Task created = service.createTask("서비스 테스트", "생성과 저장");

        assertTrue(created.getId() > 0);
        assertEquals("서비스 테스트", created.getTitle());
        assertEquals("생성과 저장", created.getDescription());
        assertEquals(TaskStatus.TODO, created.getStatus());

        Task found = service.findById(created.getId());

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals("서비스 테스트", found.getTitle());
    }

    @Test
    void 업무가_없으면_빈_목록을_반환한다() {
        assertTrue(service.findAll().isEmpty());
    }

    @Test
    void 생성한_순서대로_목록을_조회한다() {
        Task first = service.createTask("첫 업무", "");
        Task second = service.createTask("두 번째 업무", "");

        List<Task> tasks = service.findAll();

        assertEquals(2, tasks.size());
        assertEquals(first.getId(), tasks.get(0).getId());
        assertEquals(second.getId(), tasks.get(1).getId());
    }

    @Test
    void 없는_ID를_조회하면_null을_반환한다() {
        assertNull(service.findById(999));
    }

    @Test
    void 잘못된_제목으로는_업무를_저장하지_않는다() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.createTask("", "설명")
        );

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    void 업무를_시작하면_조회한_상태가_IN_PROGRESS다() {
        Task task = service.createTask("시작 테스트", "");

        service.startTask(task);

        assertEquals(TaskStatus.IN_PROGRESS, service.findById(task.getId()).getStatus());
    }

    @Test
    void 시작한_업무를_완료하면_조회한_상태가_DONE이다() {
        Task task = service.createTask("완료 테스트", "");
        service.startTask(task);

        service.completeTask(task);
        assertEquals(TaskStatus.DONE, service.findById(task.getId()).getStatus());
    }

    @Test
    void 시작_전_완료하면_예외가_발생하고_TODO를_유지한다() {
        Task task = service.createTask("시작 전 완료 테스트", "");

        assertThrows(IllegalStateException.class, () -> service.completeTask(task));
        assertEquals(TaskStatus.TODO, service.findById(task.getId()).getStatus());
    }

    @Test
    void 중복_시작하면_예외가_발생하고_IN_PROGRESS를_유지한다() {
        Task task = service.createTask("중복 시작 테스트", "");
        service.startTask(task);

        assertThrows(IllegalStateException.class, () -> service.startTask(task));
        assertEquals(TaskStatus.IN_PROGRESS, service.findById(task.getId()).getStatus());
    }

    @Test
    void 중복_완료하면_예외가_발생하고_DONE을_유지한다() {
        Task task = service.createTask("중복 완료 테스트", "");
        service.startTask(task);
        service.completeTask(task);

        assertThrows(IllegalStateException.class, () -> service.completeTask(task));
        assertEquals(TaskStatus.DONE, service.findById(task.getId()).getStatus());
    }
}