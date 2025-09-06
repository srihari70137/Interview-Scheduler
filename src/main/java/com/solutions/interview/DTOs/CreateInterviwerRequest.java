package com.solutions.interview.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInterviwerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String expertise;
}
