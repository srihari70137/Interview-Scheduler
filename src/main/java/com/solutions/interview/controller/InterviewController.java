package com.solutions.interview.controller;

import com.solutions.interview.DTOs.CreateInterviewRequest;
import com.solutions.interview.DTOs.FeedbackRequest;
import com.solutions.interview.DTOs.InterviewDto;
import com.solutions.interview.DTOs.InterviewSearchCriteria;
import com.solutions.interview.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interviews")
public class InterviewController {
    @Autowired
    InterviewService interviewService;

    @PostMapping
    public ResponseEntity<InterviewDto> scheduleInterview(@RequestBody CreateInterviewRequest createInterviewRequest){
        InterviewDto interview=interviewService.scheduleInterview(createInterviewRequest);
        return ResponseEntity.status(201).body(interview);
    }

    @PostMapping("{id}/feedback")
    public ResponseEntity<InterviewDto> submitFeedback(@PathVariable("id") Long interviewId, @RequestBody FeedbackRequest feedbackRequest){
        InterviewDto interview=interviewService.submitFeedback(interviewId, feedbackRequest);
        return ResponseEntity.status(201).body(interview);
    }
    @GetMapping
    public ResponseEntity<Page<InterviewDto>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String candidateName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "scheduledAt") String sortBy
    )
    {
        System.out.println("Searching interviews with name: " + name + ", candidateName: " + candidateName + ", page: " + page + ", size: " + size + ", sortBy: " + sortBy);
        InterviewSearchCriteria interviewSearchCriteria=new InterviewSearchCriteria(name,candidateName);
        Pageable pageable= PageRequest.of(page,size, Sort.by(sortBy).descending());
        Page<InterviewDto> searchResults=interviewService.searchInterviews(interviewSearchCriteria, pageable);
        return ResponseEntity.ok(searchResults);
    }
}
