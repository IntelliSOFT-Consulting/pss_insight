package com.intellisoft.nationalinstance.db;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "data_entry_response")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataEntryResponses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long dataEntryId;
    private String indicator;
    private String response;
    private String comment;
    private String attachment;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;


}
