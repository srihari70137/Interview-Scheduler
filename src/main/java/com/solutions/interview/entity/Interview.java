package com.solutions.interview.entity;

import com.solutions.interview.enums.InterviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime scheduledAt;
    @Enumerated(EnumType.STRING)
    private InterviewStatus status;
    private Integer durationMinutes;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "interviewer_id", nullable = false)
    private Interviewer interviewer;

    @OneToOne(mappedBy = "interview", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Feedback feedback;

}
