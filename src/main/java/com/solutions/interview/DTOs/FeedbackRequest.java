package com.solutions.interview.DTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackRequest {
    @NonNull
   private short feedbackRating;
   private String comments;
}
