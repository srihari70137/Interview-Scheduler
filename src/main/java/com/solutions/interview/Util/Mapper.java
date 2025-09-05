package com.solutions.interview.Util;

import com.solutions.interview.DTOs.InterviewDto;
import com.solutions.interview.entity.Interview;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class Mapper {
    public static InterviewDto convertToDto(Interview interview){
        if(interview == null) return null;
        InterviewDto dto=new InterviewDto();
        dto.setId(interview.getId());
        dto.setCandidateId(interview.getCandidate()!=null?interview.getCandidate().getId():null);
        dto.setInterviewerId(interview.getInterviewer()!=null?interview.getInterviewer().getId():null);
        dto.setScheduledAt(interview.getScheduledAt()!=null?interview.getScheduledAt():null);
        dto.setDurationMinutes(interview.getDurationMinutes()!=null?interview.getDurationMinutes():null);
        dto.setStatus(interview.getStatus());
        if(interview.getFeedback() != null){
            InterviewDto.FeedbackDto feedbackDto=new InterviewDto.FeedbackDto();
            feedbackDto.setId(interview.getFeedback().getId());
            feedbackDto.setRating(interview.getFeedback().getRating());
            feedbackDto.setComments(interview.getFeedback().getComments());
            dto.setFeedback(feedbackDto);
        }
    return dto;
    }

}
