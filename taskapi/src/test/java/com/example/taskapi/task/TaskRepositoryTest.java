package com.example.taskapi.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryTest {

    @Test
    void 저장하면_서로_다른_양수_ID가_부여된다() {
        TaskRepository repository = new TaskRepository();
        Task first = new Task("첫 업무", "");
        Task second = new Task("두 번째 업무", "");

        repository.save(first);
        repository.save(second);

        assertTrue(first.getId() > 0);
        assertTrue(second.getId() > 0);
        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    void 저장한_ID로_해당_업무를_조회한다() {
        TaskRepository repository = new TaskRepository();
        Task first = repository.save(new Task("첫 업무", ""));
        Task second = repository.save(new Task("두 번째 업무", ""));

        Task found = repository.findById(first.getId());

        assertNotNull(found);
        assertEquals(first.getId(), found.getId());
        assertEquals("첫 업무", found.getTitle());

        Task secondFound = repository.findById(second.getId());

        assertNotNull(secondFound);
        assertEquals(second.getId(), secondFound.getId());
        assertEquals("두 번째 업무", secondFound.getTitle());
    }

    @Test
    void 없는_ID를_조회하면_null을_반환한다() {
        TaskRepository repository = new TaskRepository();
        repository.save(new Task("첫 업무", ""));

        Task found = repository.findById(999);

        assertNull(found);
    }
}