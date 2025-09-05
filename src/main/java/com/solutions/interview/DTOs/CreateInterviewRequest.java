package com.solutions.interview.DTOs;
import jakarta.validation.constraints.Min;
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
        @NotNull
        private Long candidateId;
        @NotNull
        private Long interviewerId;
        @NotNull
        private LocalDateTime scheduledAt;
        @NotNull @Min(15)
        private Integer durationMinutes;
}
