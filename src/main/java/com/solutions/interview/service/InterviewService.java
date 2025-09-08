package com.solutions.interview.service;

import com.solutions.interview.DTOs.*;
import com.solutions.interview.entity.Candidate;
import com.solutions.interview.entity.Interviewer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface InterviewService {

     InterviewDto scheduleInterview(CreateInterviewRequest createInterviewRequest);
     public InterviewDto cancelInterview(Long interviewId);
     Page searchInterviews(InterviewSearchCriteria interviewSearchCriteria, Pageable pageable);
     InterviewDto submitFeedback(Long interviewId, FeedbackRequest feedbackRequest);
     Interviewer createInterviewerifNotExists(CreateInterviwerRequest createInterviwerRequest);
     Candidate createCandidateIfNotExists(CreateCandidateRequest createCandidateRequest);
     public List<Candidate> getAllCandidates();
     public List<Interviewer> getAllInterviewers();

}