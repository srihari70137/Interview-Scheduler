package com.solutions.interview.model.repository;

import com.solutions.interview.model.entity.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {
    Optional<Interviewer> findByEmail(String email);
}
