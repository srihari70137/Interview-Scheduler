package com.solutions.interview.controller;

import com.solutions.interview.DTOs.*;
import com.solutions.interview.entity.Candidate;
import com.solutions.interview.entity.Interviewer;
import com.solutions.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
public class InterviewController {
    @Autowired
    InterviewService interviewService;

    private static final String DEFAULT_TO = "kksrihari143@gmail.com";
    private final JavaMailSender mailSender;


    @GetMapping("/healthz")
    public ResponseEntity<String> healthCheck() {
        System.out.println("Health check at " + Calendar.getInstance().getTime());
        return ResponseEntity.ok("Interview Scheduling Service is up and running!");
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<Candidate>> getAllCandidates(){
        System.out.println("Fetching all candidates at " + Calendar.getInstance().getTime());
        return ResponseEntity.ok(interviewService.getAllCandidates());
    }


    @GetMapping("/interviewers")
    public ResponseEntity<List<Interviewer>> getAllInterviewers(){
        System.out.println("Fetching all Interviewers at " + Calendar.getInstance().getTime());
        return ResponseEntity.ok(interviewService.getAllInterviewers());
    }


    @PostMapping("/createCandidate")
    public ResponseEntity<String> createCandidate(@RequestBody CreateCandidateRequest createCandidateRequest){
        interviewService.createCandidateIfNotExists(createCandidateRequest);
        return ResponseEntity.ok("Candidate profile has been created successfully ");
    }


    @PostMapping("/createInterviewer")
    public ResponseEntity<String> createInterviewer(@RequestBody CreateInterviwerRequest createInterviewRequest ){
        interviewService.createInterviewerifNotExists(createInterviewRequest);
        return ResponseEntity.ok("Candidate profile has been created successfully ");
    }

    @PostMapping("/schedule")
    public ResponseEntity<InterviewDto> scheduleInterview(@RequestBody CreateInterviewRequest createInterviewRequest){
        InterviewDto interview=interviewService.scheduleInterview(createInterviewRequest);
        return ResponseEntity.status(201).body(interview);
    }

    @PostMapping("/cancel")
    public ResponseEntity<InterviewDto> cancelInterview(@RequestBody  Long interviewId){
        InterviewDto interview=interviewService.cancelInterview(interviewId);
        return ResponseEntity.status(201).body(interview);
    }

    @PostMapping("{id}/feedback")
    public ResponseEntity<InterviewDto> submitFeedback(@PathVariable("id") Long interviewId, @RequestBody FeedbackRequest feedbackRequest){
        InterviewDto interview=interviewService.submitFeedback(interviewId, feedbackRequest);
        return ResponseEntity.status(201).body(interview);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<SearchDto>> search(
            @RequestParam(value = "interviewerName", required = false) String interviewerName,
            @RequestParam(value = "candidateName", required = false) String candidateName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "scheduledAt") String sortBy
    )
    {
        System.out.println("Searching interviews with interviewerName: " + interviewerName + ", candidateName: " + candidateName + ", page: " + page + ", size: " + size + ", sortBy: " + sortBy);
        InterviewSearchCriteria interviewSearchCriteria=new InterviewSearchCriteria(interviewerName,candidateName);
        System.out.println("Interview Search Criteria: " + interviewSearchCriteria);
        Pageable pageable= PageRequest.of(page,size, Sort.by(sortBy).descending());
        Page<SearchDto> searchResults=interviewService.searchInterviews(interviewSearchCriteria, pageable);
        return ResponseEntity.ok(searchResults);
    }




    @GetMapping("/sendNotification")
        public String sendTestEmail(@RequestParam String to) {
        System.out.println("Sending test email to: " + to);
            if(to == null || to.isEmpty()) {
                to = DEFAULT_TO; // default email address
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Test Email - Interview Scheduler");
            message.setText("Hello, this is a test email from your Interview Scheduler app!");
            message.setFrom("sriharikumbha070422@gmail.com"); // must match spring.mail.username

            try {
                mailSender.send(message);
                return "Test email sent successfully to " + to;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed to send email: " + e.getMessage();
            }
        }
    }
