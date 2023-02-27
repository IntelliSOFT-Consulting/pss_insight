package com.intellisoft.nationalinstance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespondentIndicators {
    private List<String> indicators;
    private String emailAddress;
    private String verificationUrl;
    private LocalDateTime expiryDateTime;
}
