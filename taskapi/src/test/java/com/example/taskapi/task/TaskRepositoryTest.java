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

    @Test
    void 가운데_업무를_삭제하면_나머지_업무와_순서를_유지한다() {
        TaskRepository repository = new TaskRepository();
        Task first = repository.save(new Task("첫 업무", "첫 설명"));
        Task middle = repository.save(new Task("가운데 업무", ""));
        Task last = repository.save(new Task("마지막 업무", "마지막 설명"));
        int firstId = first.getId();
        int deletedId = middle.getId();
        int lastId = last.getId();

        repository.deleteTask(middle);

        assertNull(repository.findById(deletedId));
        assertEquals(2, repository.findAll().size());
        assertEquals(firstId, repository.findAll().get(0).getId());
        assertEquals(lastId, repository.findAll().get(1).getId());
        assertEquals("첫 업무", repository.findById(firstId).getTitle());
        assertEquals("첫 설명", repository.findById(firstId).getDescription());
        assertEquals("마지막 업무", repository.findById(lastId).getTitle());
        assertEquals("마지막 설명", repository.findById(lastId).getDescription());
    }

    @Test
    void 마지막_업무를_삭제해도_새_업무는_삭제한_ID를_재사용하지_않는다() {
        TaskRepository repository = new TaskRepository();
        Task first = repository.save(new Task("첫 업무", ""));
        Task last = repository.save(new Task("삭제할 업무", ""));
        int firstId = first.getId();
        int deletedId = last.getId();

        repository.deleteTask(last);
        Task created = repository.save(new Task("새 업무", ""));

        assertTrue(created.getId() > 0);
        assertNotEquals(firstId, created.getId());
        assertNotEquals(deletedId, created.getId());
        assertNull(repository.findById(deletedId));
        assertEquals(2, repository.findAll().size());
        assertEquals(firstId, repository.findAll().get(0).getId());
        assertEquals(created.getId(), repository.findAll().get(1).getId());
    }

    @Test
    void 저장되지_않은_업무를_삭제해도_기존_업무는_유지된다() {
        TaskRepository repository = new TaskRepository();
        Task saved = repository.save(new Task("저장한 업무", "유지할 설명"));
        int originalId = saved.getId();
        Task unsaved = new Task("저장하지 않은 업무", "");

        repository.deleteTask(unsaved);

        assertEquals(1, repository.findAll().size());
        Task found = repository.findById(originalId);
        assertNotNull(found);
        assertEquals(originalId, found.getId());
        assertEquals("저장한 업무", found.getTitle());
        assertEquals("유지할 설명", found.getDescription());
        assertEquals(TaskStatus.TODO, found.getStatus());
    }
}