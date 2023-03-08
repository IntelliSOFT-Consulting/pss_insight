package com.intellisoft.nationalinstance.db;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

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
