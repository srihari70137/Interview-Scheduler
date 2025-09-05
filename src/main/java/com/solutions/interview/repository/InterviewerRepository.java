package com.solutions.interview.repository;

import com.solutions.interview.entity.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {
}
