package com.example.taskapi.task;

import jakarta.validation.constraints.NotBlank;

public record TaskUpdateRequest(
        @NotBlank(message = "제목은 비어 있을 수 없습니다.")
        String title,
        String description
) {
}
