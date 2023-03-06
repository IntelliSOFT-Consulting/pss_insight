package com.intellisoft.nationalinstance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OutgoingAnswers {
    private String program="eBAyeGv0exc";
    private String orgUnit="DiszpKrYNg8";
    private String storedBy="admin";
    private String eventDate;
    private String status;
    private List<DataValues> dataValues;
}
