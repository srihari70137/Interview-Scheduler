package com.solutions.interview.model.repository;

import com.solutions.interview.model.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface InterviewRepository extends JpaRepository<Interview, Long>, JpaSpecificationExecutor<Interview> {

    /**
     * Simple check: exact start time clash for the interviewer
     * Returns true if interviewer already has an interview at the same start time
     */
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM Interview i " +
            "WHERE i.interviewer.id = :interviewerId " +
            "AND i.status = 'SCHEDULED' " +
            "AND i.scheduledAt = :startTime")
    boolean existsByInterviewerIdAndScheduledAt(
            @Param("interviewerId") Long interviewerId,
            @Param("startTime") LocalDateTime startTime
    );
}
