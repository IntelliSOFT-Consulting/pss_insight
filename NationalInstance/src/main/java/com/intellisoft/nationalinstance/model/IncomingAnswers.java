package com.intellisoft.nationalinstance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomingAnswers {
    private String eventDate;
    private String status;
    private List<DataValues> dataValues;
    private String comments;
}
