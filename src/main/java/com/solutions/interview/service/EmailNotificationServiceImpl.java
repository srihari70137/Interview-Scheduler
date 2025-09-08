package com.solutions.interview.service;

import com.solutions.interview.entity.Candidate;
import com.solutions.interview.entity.Interviewer;
import com.solutions.interview.entity.Interview;
import com.solutions.interview.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendInterviewScheduled(Candidate candidate, Interviewer interviewer, Interview interview) {
        String subject = "Interview Scheduled";
        String text = String.format(
                "Dear %s,\n\nYour interview with %s is scheduled on %s for %d minutes.\n\nRegards,\nInterview Team",
                candidate.getFirstName(),
                interviewer.getFirstName() + " " + interviewer.getLastName(),
                interview.getScheduledAt(),
                interview.getDurationMinutes()
        );
        sendEmail(candidate.getEmail(), subject, text);

        String interviewerText = String.format(
                "Dear %s,\n\nYou have an interview with %s scheduled on %s for %d minutes.\n\nRegards,\nInterview Team",
                interviewer.getFirstName(),
                candidate.getFirstName() + " " + candidate.getLastName(),
                interview.getScheduledAt(),
                interview.getDurationMinutes()
        );
        sendEmail(interviewer.getEmail(), subject, interviewerText);
    }

    @Override
    public void sendInterviewCancelled(Candidate candidate, Interviewer interviewer, Interview interview) {
        String subject = "Interview Cancelled";
        String text = String.format(
                "Dear %s,\n\nYour interview with %s scheduled on %s has been cancelled.\n\nRegards,\nInterview Team",
                candidate.getFirstName(),
                interviewer.getFirstName() + " " + interviewer.getLastName(),
                interview.getScheduledAt()
        );
        sendEmail(candidate.getEmail(), subject, text);
        sendEmail(interviewer.getEmail(), subject, text);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
