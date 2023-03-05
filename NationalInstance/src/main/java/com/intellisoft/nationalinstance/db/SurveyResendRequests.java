package com.intellisoft.nationalinstance.db;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "survey_resend_requests")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SurveyResendRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String respondentId;

    private String comment;

    private String status;

}
