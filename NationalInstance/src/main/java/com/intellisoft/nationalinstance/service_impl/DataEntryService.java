package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.DbDataEntryData;
import com.intellisoft.nationalinstance.Results;

public interface DataEntryService {

    Results addDataEntry(DbDataEntryData dbDataEntryData);
    Results listDataEntry(int no, int size, String status, String dataEntryPersonId);
    Results viewDataEntry(Long id);
    Results updateDataEntry(Long id, DbDataEntryData dbDataEntryData);
    Results deleteDataEntry(Long id);


}
