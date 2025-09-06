package com.solutions.interview.specification;

import com.solutions.interview.entity.Interview;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public final class InterviewSpecification {
    private InterviewSpecification() {}

    public static Specification<Interview> interviewerNameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            Join<Object, Object> interviewer = root.join("interviewer", JoinType.INNER);
            return cb.or(
                    cb.like(cb.lower(interviewer.get("firstName")), "%" + name.toLowerCase() + "%"),
                    cb.like(cb.lower(interviewer.get("lastName")), "%" + name.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Interview> candidateNameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            Join<Object, Object> candidate = root.join("candidate", JoinType.INNER);
            return cb.or(
                    cb.like(cb.lower(candidate.get("firstName")), "%" + name.toLowerCase() + "%"),
                    cb.like(cb.lower(candidate.get("lastName")), "%" + name.toLowerCase() + "%")
            );
        };
    }
}
