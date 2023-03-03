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
@Table(name = "data_entry")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String selectedPeriod;

    private String status; // DRAFT / SUBMITTED
    private String dataEntryPersonId; //The person
    private String dataEntryDate; // The format is yyyymmdd
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;


}
