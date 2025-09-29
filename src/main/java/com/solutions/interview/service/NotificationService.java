package com.solutions.interview.service;

import com.solutions.interview.model.entity.Candidate;
import com.solutions.interview.model.entity.Interview;
import com.solutions.interview.model.entity.Interviewer;

public interface NotificationService {
    void sendInterviewScheduled(Candidate candidate, Interviewer interviewer, Interview interview);
    void sendInterviewCancelled(Candidate candidate, Interviewer interviewer, Interview interview);
}
