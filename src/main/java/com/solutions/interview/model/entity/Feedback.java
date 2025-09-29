package com.solutions.interview.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "rating")
    private short rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="interview_id", nullable = false, unique = true)
    private Interview interview;
}
