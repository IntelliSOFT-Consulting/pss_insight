package com.intellisoft.internationalinstance.db;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "metadata_json")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MetadataJson {

    @Id
    private String id;

    private String code;

    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    @Column(length = 8000)
    private String metadata;
}
