package com.example.taskapi.task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTaskRepository extends JpaRepository<Task, Integer> {

}