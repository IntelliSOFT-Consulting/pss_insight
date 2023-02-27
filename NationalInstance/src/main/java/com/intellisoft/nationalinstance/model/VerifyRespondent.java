package com.intellisoft.nationalinstance.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VerifyRespondent {
    private String otp;
    private Long surveyId;
}
