package com.intellisoft.nationalinstance.db;

import lombok.*;

@Entity
@Table(name = "indicators")
@Builder@Setter@Getter@AllArgsConstructor@NoArgsConstructor@ToString
public class Indicators {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String indicatorId;
    @Column(length = 8000)
    private String metadata;


}
