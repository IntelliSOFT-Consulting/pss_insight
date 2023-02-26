package com.intellisoft.nationalinstance.db;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "versions")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VersionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String versionName;
    private String versionDescription;
    private Boolean isPublished;
    @ElementCollection
    private List<String> indicators;
}
