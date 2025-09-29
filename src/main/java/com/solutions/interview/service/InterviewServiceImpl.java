package com.solutions.interview.service;

import com.solutions.interview.view.Util.Mapper;
import com.solutions.interview.model.entity.Candidate;
import com.solutions.interview.model.entity.Feedback;
import com.solutions.interview.model.entity.Interview;
import com.solutions.interview.model.entity.Interviewer;
import com.solutions.interview.model.enums.InterviewStatus;
import com.solutions.interview.exceptionHandling.BusinessException;
import com.solutions.interview.model.repository.CandidateRepository;
import com.solutions.interview.model.repository.FeedbackRepository;
import com.solutions.interview.model.repository.InterviewRepository;
import com.solutions.interview.model.repository.InterviewerRepository;
import com.solutions.interview.service.specification.InterviewSpecification;
import com.solutions.interview.view.dto.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.solutions.interview.view.Util.Mapper.convertToDto;

@Service
@AllArgsConstructor
public class InterviewServiceImpl implements InterviewService{
    CandidateRepository candidateRepository;
    FeedbackRepository feedbackRepository;
    InterviewerRepository interviewerRepository;
    InterviewRepository interviewRepository;

    @Override
    public Interviewer createInterviewerifNotExists(CreateInterviwerRequest request) {
        interviewerRepository.findByEmail(request.getEmail()).ifPresent(c -> {
            throw new BusinessException(
                    "Candidate with email " + request.getEmail() + " already exists",
                    HttpStatus.BAD_REQUEST // Bad Request
            );
        });
                    Interviewer interviewer = new Interviewer();
                    interviewer.setFirstName(request.getFirstName());
                    interviewer.setLastName(request.getLastName());
                    interviewer.setEmail(request.getEmail());
                    interviewer.setExpertise(request.getExpertise());
                    return interviewerRepository.save(interviewer);
    }

    @Override
    public Candidate createCandidateIfNotExists(CreateCandidateRequest request) {
        candidateRepository.findByEmail(request.getEmail()).ifPresent(c -> {
            throw new BusinessException(
                    "Candidate with email " + request.getEmail() + " already exists",
                    HttpStatus.BAD_REQUEST // Bad Request
            );
        });
                Candidate candidate = new Candidate();
                    candidate.setFirstName(request.getFirstName());
                    candidate.setLastName(request.getLastName());
                    candidate.setEmail(request.getEmail());
                    candidate.setPhone(request.getPhone());
                    return candidateRepository.save(candidate);
    }


    @Override
    @Transactional
    public InterviewDto scheduleInterview(CreateInterviewRequest request) {

        if (request.getCandidateId() == null || request.getInterviewerId() == null) {
            throw new BusinessException(
                    "Candidate ID and Interviewer ID must not be null",
                    HttpStatus.BAD_REQUEST //400
            );
        }

        if (request.getScheduledAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Interview cannot be scheduled in the past"); //400
        }

        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new BusinessException("Candidate not found", HttpStatus.NOT_FOUND)); //404

        Interviewer interviewer = interviewerRepository.findById(request.getInterviewerId())
                .orElseThrow(() -> new BusinessException("Interviewer not found", HttpStatus.NOT_FOUND)); //404

        boolean isBusy = interviewRepository.existsByInterviewerIdAndScheduledAt(
                interviewer.getId(),
                request.getScheduledAt()
        );
        System.out.println("is Busy :: "+ isBusy);

        if (isBusy) {
            throw new BusinessException(
                    "Interviewer is busy at this scheduled time",
                    HttpStatus.CONFLICT
            );
        }

        Interview interview = Interview.builder()
                .candidate(candidate)
                .interviewer(interviewer)
                .scheduledAt(request.getScheduledAt())
                .durationMinutes(request.getDurationMinutes())
                .status(InterviewStatus.SCHEDULED)
                .build();

        Interview savedInterview = interviewRepository.save(interview);
        // notificationService.sendInterviewScheduled(candidate, interviewer, savedInterview);

        return convertToDto(savedInterview);
    }


    @Override
    @Transactional
    public InterviewDto cancelInterview(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new BusinessException("Interview not found"));

        if (interview.getStatus() == InterviewStatus.COMPLETED) {
            throw new BusinessException("Cannot cancel an interview that is already COMPLETED");
        }
        if (interview.getStatus() == InterviewStatus.CANCELLED) {
            throw new BusinessException("Interview is already CANCELLED");
        }

        interview.setStatus(InterviewStatus.CANCELLED);
        Interview savedInterview = interviewRepository.save(interview);
        // notificationService.sendInterviewCancelled(interview.getCandidate(), interview.getInterviewer(), savedInterview);

        return convertToDto(savedInterview);
    }


    @Override
    @Transactional
    public InterviewDto submitFeedback(Long interviewId, FeedbackRequest feedbackRequest) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new BusinessException("Interview not found", HttpStatus.NOT_FOUND));

        if (interview.getStatus() != InterviewStatus.SCHEDULED) {
            throw new BusinessException("Feedback can only be submitted for SCHEDULED interviews");
        }

        if (interview.getFeedback() != null) {
            throw new BusinessException("Feedback has already been submitted for this interview");
        }

        Feedback feedback = Feedback.builder()
                .interview(interview)
                .rating(feedbackRequest.getFeedbackRating())
                .comments(feedbackRequest.getComments())
                .build();

        interview.setFeedback(feedbackRepository.save(feedback));
        interview.setStatus(InterviewStatus.COMPLETED);

        return convertToDto(interviewRepository.save(interview));
    }


    @Override
    public Page<SearchDto> searchInterviews(InterviewSearchCriteria criteria, Pageable pageable) {
        Specification<Interview> spec = Specification.allOf(
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

