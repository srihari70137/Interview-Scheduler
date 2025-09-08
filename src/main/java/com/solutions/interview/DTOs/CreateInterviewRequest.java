package com.solutions.interview.DTOs;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class CreateInterviewRequest {
        @NotNull @NotBlank
        private Long candidateId;
        @NotNull @NotBlank
        private Long interviewerId;
        @NotNull @NotBlank
        private LocalDateTime scheduledAt;
        @NotNull @Min(15) @NotBlank
        private Integer durationMinutes;
}
