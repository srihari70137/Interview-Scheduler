package com.solutions.interview.DTOs;

import com.solutions.interview.enums.InterviewStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDto {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private Long interviewerId;
    private String interviewerName;
    private LocalDateTime scheduledAt;
    private Integer durationMinutes;
    private InterviewStatus status;
    private FeedbackDto feedback;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedbackDto {
        private Long id;
        private int rating;
        private String comments;
    }
}
