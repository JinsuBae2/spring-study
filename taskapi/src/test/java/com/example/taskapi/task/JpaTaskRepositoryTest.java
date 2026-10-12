package com.example.taskapi.task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaTaskRepositoryTest {

    @Autowired
    private JpaTaskRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void 저장한_업무를_DB에서_다시_조회한다() {
        Task task = new Task("JPA 공부", "DB 저장과 조회");

        repository.save(task);

        entityManager.flush();
        entityManager.clear();

        Task found = repository.findById(task.getId()).orElseThrow();

        assertTrue(task.getId() > 0);
        assertEquals(task.getId(), found.getId());
        assertEquals("JPA 공부", found.getTitle());
        assertEquals("DB 저장과 조회", found.getDescription());
        assertEquals(TaskStatus.TODO, found.getStatus());
    }

    @Test
    void 두_업무를_저장하면_서로_다른_양수_ID가_생성된다() {
        Task first = new Task("첫 업무", "첫 설명");
        Task second = new Task("두 번째 업무", "두 번째 설명");

        repository.save(first);
        repository.save(second);

        assertTrue(first.getId() > 0);
        assertTrue(second.getId() > 0);
        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    void 없는_ID를_조회하면_빈_Optional을_반환한다() {
        int missingId = -1;

        assertTrue(repository.findById(missingId).isEmpty());
    }
}
