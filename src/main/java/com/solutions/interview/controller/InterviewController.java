package com.solutions.interview.controller;

import com.solutions.interview.model.entity.Candidate;
import com.solutions.interview.model.entity.Interviewer;
import com.solutions.interview.service.InterviewService;
import com.solutions.interview.view.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;
    private final JavaMailSender mailSender;

    private static final String DEFAULT_TO = "kksrihari143@gmail.com";

    @GetMapping("/healthz")
    public ResponseEntity<String> healthCheck() {
        System.out.println("Health check at " + Calendar.getInstance().getTime());
        return ResponseEntity.ok("Interview Scheduling Service is up and running!");
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(interviewService.getAllCandidates());
    }

    @GetMapping("/interviewers")
    public ResponseEntity<List<Interviewer>> getAllInterviewers() {
        return ResponseEntity.ok(interviewService.getAllInterviewers());
    }

    @PostMapping("/createCandidate")
    public ResponseEntity<String> createCandidate(@RequestBody CreateCandidateRequest request) {
        interviewService.createCandidateIfNotExists(request);
        return ResponseEntity.ok("Candidate profile has been created successfully");
    }

    @PostMapping("/createInterviewer")
    public ResponseEntity<String> createInterviewer(@RequestBody CreateInterviwerRequest request) {
        interviewService.createInterviewerifNotExists(request);
        return ResponseEntity.ok("Interviewer profile has been created successfully");
    }

    @PostMapping("/schedule")
    public ResponseEntity<InterviewDto> scheduleInterview(@RequestBody CreateInterviewRequest request) {
        InterviewDto interview = interviewService.scheduleInterview(request);
        return ResponseEntity.status(201).body(interview);
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<InterviewDto> cancelInterview(@PathVariable(value = "id") Long id) {
        InterviewDto interview = interviewService.cancelInterview(id);
        return ResponseEntity.ok(interview);
    }

    @PostMapping("/{id}/feedback")
    public ResponseEntity<InterviewDto> submitFeedback(
            @PathVariable("id") Long id,
            @RequestBody FeedbackRequest feedbackRequest) {
        InterviewDto interview = interviewService.submitFeedback(id, feedbackRequest);
        return ResponseEntity.status(201).body(interview);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SearchDto>> search(
            @RequestParam(value = "interviewerName", required = false) String interviewerName,
            @RequestParam(value = "candidateName", required = false) String candidateName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "scheduledAt") String sortBy
    ) {
        InterviewSearchCriteria criteria = new InterviewSearchCriteria(interviewerName, candidateName);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<SearchDto> searchResults = interviewService.searchInterviews(criteria, pageable);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/sendNotification")
    public String sendTestEmail(@RequestParam(value = "to", required = false) String to) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("smtp.gmail.com", 587), 5000);
            System.out.println("Port 587 is OPEN ");
        } catch (IOException e) {
            System.out.println("Port 587 is BLOCKED  : " + e.getMessage());
        }
        if (to == null || to.isEmpty()) {
            to = DEFAULT_TO;
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
