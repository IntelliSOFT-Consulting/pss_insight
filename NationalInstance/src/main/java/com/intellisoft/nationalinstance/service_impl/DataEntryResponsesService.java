package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.DbDataEntryResponses;
import com.intellisoft.nationalinstance.db.DataEntryResponses;

import java.util.List;

public interface DataEntryResponsesService {

    void addDataEntryResponses(List<DataEntryResponses> dbDataEntryResponses);
    List<DataEntryResponses> listDataEntryResponses(Long dataEntryId);
    DataEntryResponses updateDataEntryResponses(Long id, DbDataEntryResponses dataEntryResponses);
    void deleteDataEntryResponses(Long id);
}
