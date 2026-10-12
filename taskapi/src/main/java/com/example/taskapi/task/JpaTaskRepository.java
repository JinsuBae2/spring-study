package com.example.taskapi.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByOrderByIdAsc();
}