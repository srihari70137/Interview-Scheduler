package com.solutions.interview.view.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSearchCriteria {
    private String interviewerName;
    private String candidateName;
}
