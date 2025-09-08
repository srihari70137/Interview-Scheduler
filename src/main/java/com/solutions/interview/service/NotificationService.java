package com.solutions.interview.service;

import com.solutions.interview.entity.Candidate;
import com.solutions.interview.entity.Interview;
import com.solutions.interview.entity.Interviewer;

public interface NotificationService {
    void sendInterviewScheduled(Candidate candidate, Interviewer interviewer, Interview interview);
    void sendInterviewCancelled(Candidate candidate, Interviewer interviewer, Interview interview);
}
