package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.DbDataEntryData;
import com.intellisoft.nationalinstance.Results;

import java.net.URISyntaxException;

public interface DataEntryService {

    Results addDataEntry(DbDataEntryData dbDataEntryData) throws URISyntaxException;
    Results listDataEntry(int no, int size, String status, String dataEntryPersonId);
    Results viewDataEntry(Long id);
    Results updateDataEntry(Long id, DbDataEntryData dbDataEntryData);
    Results deleteDataEntry(Long id);


}
