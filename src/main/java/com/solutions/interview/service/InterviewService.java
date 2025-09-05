package com.solutions.interview.service;

import com.solutions.interview.DTOs.CreateInterviewRequest;
import com.solutions.interview.DTOs.FeedbackRequest;
import com.solutions.interview.DTOs.InterviewDto;
import com.solutions.interview.DTOs.InterviewSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface InterviewService {

     InterviewDto scheduleInterview(CreateInterviewRequest createInterviewRequest);
     Page searchInterviews(InterviewSearchCriteria interviewSearchCriteria, Pageable pageable);
     InterviewDto submitFeedback(Long interviewId, FeedbackRequest feedbackRequest);

}