package com.intellisoft.nationalinstance.db;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "respondent_questions")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RespondentQuestions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String otp;
    private LocalDateTime expiryDate;
    @Column(length = 8000)
    private boolean verified=false;
    private String comments;
    @ElementCollection
    private List<String> indicators;
}
