package com.solutions.interview.service;

import com.solutions.interview.DTOs.*;
import com.solutions.interview.Util.Mapper;
import com.solutions.interview.entity.Candidate;
import com.solutions.interview.entity.Feedback;
import com.solutions.interview.entity.Interview;
import com.solutions.interview.entity.Interviewer;
import com.solutions.interview.enums.InterviewStatus;
import com.solutions.interview.exceptionHandling.NotFoundException;
import com.solutions.interview.repository.CandidateRepository;
import com.solutions.interview.repository.FeedbackRepository;
import com.solutions.interview.repository.InterviewRepository;
import com.solutions.interview.repository.InterviewerRepository;
import com.solutions.interview.specification.InterviewSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.solutions.interview.Util.Mapper.convertToDto;

@Service
@AllArgsConstructor
public class InterviewServiceImpl implements InterviewService{
    CandidateRepository candidateRepository;
    FeedbackRepository feedbackRepository;
    InterviewerRepository interviewerRepository;
    InterviewRepository interviewRepository;
    private final NotificationService notificationService;

    @Override
    public Interviewer createInterviewerifNotExists(CreateInterviwerRequest createInterviwerRequest) {
        Interviewer interviewer=new Interviewer();
       interviewer.setFirstName(createInterviwerRequest.getFirstName());
       interviewer.setLastName(createInterviwerRequest.getLastName());
       interviewer.setEmail(createInterviwerRequest.getEmail());
         interviewer.setExpertise(createInterviwerRequest.getExpertise());
        return interviewerRepository.save(interviewer);
    }

    @Override
    public Candidate createCandidateIfNotExists(CreateCandidateRequest createCandidateRequest) {
        Candidate candidate = new Candidate();
        candidate.setFirstName(createCandidateRequest.getFirstName());
        candidate.setLastName(createCandidateRequest.getLastName());
        candidate.setEmail(createCandidateRequest.getEmail());
        candidate.setPhone(createCandidateRequest.getPhone());

        return candidateRepository.save(candidate);
    }

    @Override
    @Transactional
    public InterviewDto scheduleInterview(CreateInterviewRequest createInterviewRequest) {
        if(createInterviewRequest.getCandidateId()==null || createInterviewRequest.getInterviewerId()==null)
            throw new IllegalArgumentException("Candidate ID and Interviewer ID must not be null");
      Candidate candidate=candidateRepository.findById(createInterviewRequest.getCandidateId()).orElseThrow(()-> new NotFoundException("Candidate not found"));
      Interviewer interviewer= interviewerRepository.findById(createInterviewRequest.getInterviewerId()).orElseThrow(()-> new NotFoundException("Interviewer not found"));
        Interview interview= Interview.builder()
                .candidate(candidate)
                .interviewer(interviewer)
                .scheduledAt(createInterviewRequest.getScheduledAt())
                .durationMinutes(createInterviewRequest.getDurationMinutes())
                .status(com.solutions.interview.enums.InterviewStatus.SCHEDULED)
                .build();
        Interview savedInterview=interviewRepository.save(interview);
       /* notificationService.sendInterviewScheduled(candidate, interviewer, savedInterview);*/
        return convertToDto(savedInterview);
    }


    @Override
    @Transactional
    public InterviewDto cancelInterview(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new NotFoundException("Interview not found"));
        interview.setStatus(InterviewStatus.CANCELLED);
        Interview savedInterview = interviewRepository.save(interview);
        /*notificationService.sendInterviewCancelled(interview.getCandidate(), interview.getInterviewer(), savedInterview);
   */return  convertToDto(savedInterview);
    }


    @Override
    @Transactional
    public InterviewDto submitFeedback(Long interviewId, FeedbackRequest feedbackRequest) {
        Interview interview=interviewRepository.findById(interviewId).orElseThrow(()-> new NotFoundException("Interview not found"));
        /*if(interview.getStatus()!=com.solutions.interview.enums.InterviewStatus.COMPLETED)
            throw new IllegalArgumentException("Feedback can only be submitted for completed interviews");*/
        if(interview.getFeedback()!=null)
            throw new IllegalArgumentException("Feedback has already been submitted for this interview");

        interview.setFeedback(feedbackRepository.save(
                Feedback.builder()
                        .interview(interview)
                        .rating(feedbackRequest.getFeedbackRating())
                        .comments(feedbackRequest.getComments())
                        .build()
        ));
        interview.setStatus(InterviewStatus.COMPLETED);
        return convertToDto(interviewRepository.save(interview));
    }

    @Override
    public Page<SearchDto> searchInterviews(InterviewSearchCriteria criteria, Pageable pageable) {
        Specification<Interview> spec = Specification.where(
                InterviewSpecification.interviewerNameContains(criteria.getInterviewerName())
        ).and(InterviewSpecification.candidateNameContains(criteria.getCandidateName()));
        Page<Interview> page = interviewRepository.findAll(spec, pageable);
        return page.map(Mapper::convertToSearchDto);

    }



    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public List<Interviewer> getAllInterviewers() {
        return interviewerRepository.findAll();
    }

}

