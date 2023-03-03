package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.internationalinstance.DbDetails;
import com.intellisoft.internationalinstance.DbResults;
import com.intellisoft.nationalinstance.DbDataEntryData;
import com.intellisoft.nationalinstance.DbDataEntryResponses;
import com.intellisoft.nationalinstance.FormatterClass;
import com.intellisoft.nationalinstance.Results;
import com.intellisoft.nationalinstance.db.DataEntry;
import com.intellisoft.nationalinstance.db.DataEntryResponses;
import com.intellisoft.nationalinstance.db.repso.DataEntryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DataEntryServiceImpl implements DataEntryService{

    private final FormatterClass formatterClass = new FormatterClass();
    private final DataEntryRepo dataEntryRepo;
    private final DataEntryResponsesService dataEntryResponsesService;

    @Override
    public Results addDataEntry(DbDataEntryData dbDataEntryData) {

        String selectedPeriod = dbDataEntryData.getSelectedPeriod();
        String status = dbDataEntryData.getStatus();
        String dataEntryPersonId = dbDataEntryData.getDataEntryPersonId();
        String dateEntryDate = getEntryDate();

        DataEntry dataEntry = new DataEntry();
        dataEntry.setStatus(status);
        dataEntry.setSelectedPeriod(selectedPeriod);
        dataEntry.setDataEntryPersonId(dataEntryPersonId);
        dataEntry.setDataEntryDate(dateEntryDate);
        DataEntry dataEntryAdded = dataEntryRepo.save(dataEntry);

        Long dataEntryId = dataEntryAdded.getId();
        List<DbDataEntryResponses> responsesList = dbDataEntryData.getResponses();
        List<DataEntryResponses> dataEntryResponsesList = new ArrayList<>();
        for (DbDataEntryResponses dbDataEntryResponses : responsesList) {

            String indicator = dbDataEntryResponses.getIndicator();
            String response = dbDataEntryResponses.getResponse();
            String comment = dbDataEntryResponses.getComment();
            String attachment = dbDataEntryResponses.getAttachment();

            DataEntryResponses dataEntryResponses = new DataEntryResponses();
            dataEntryResponses.setDataEntryId(dataEntryId);
            dataEntryResponses.setResponse(response);
            dataEntryResponses.setComment(comment);
            dataEntryResponses.setAttachment(attachment);
            dataEntryResponses.setIndicator(indicator);
            dataEntryResponsesList.add(dataEntryResponses);
        }
        dataEntryResponsesService.addDataEntryResponses(dataEntryResponsesList);


        return new Results(201, new DbDetails("Data submitted successfully."));
    }
    private String getEntryDate(){
        LocalDate currentDate = LocalDate.now();
        // Format the date to yyyymmdd format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return currentDate.format(formatter);
    }

    @Override
    public Results listDataEntry(int no, int size, String status, String dataEntryPersonId) {

        List<DataEntry> dataEntryList =
                getPagedDataEntryData(no,size,"","", status, dataEntryPersonId);

        DbResults dbResults = new DbResults(
                dataEntryList.size(),
                dataEntryList);

        return new Results(200, dbResults);
    }

    private List<DataEntry> getPagedDataEntryData(
            int pageNo,
            int pageSize,
            String sortField,
            String sortDirection,
            String status,
            String userId) {
        String sortPageField = "";
        String sortPageDirection = "";

        if (sortField.equals("")){sortPageField = "createdAt"; }else {sortPageField = sortField;}
        if (sortDirection.equals("")){sortPageDirection = "DESC"; }else {sortPageDirection = sortField;}

        Sort sort = sortPageDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortPageField).ascending() : Sort.by(sortPageField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<DataEntry> page =
                dataEntryRepo.findAllByStatusAndDataEntryPersonId(
                status, userId, pageable);

        return page.getContent();
    }

    @Override
    public Results viewDataEntry(Long id) {

        Optional<DataEntry> dataEntryOptional = dataEntryRepo.findById(id);
        if (dataEntryOptional.isPresent()){
            DataEntry dataEntry = dataEntryOptional.get();
            Long dataEntryId = dataEntry.getId();
            List<DataEntryResponses> dataEntryResponsesList =
                    dataEntryResponsesService.listDataEntryResponses(dataEntryId);

            List<DbDataEntryResponses> dbDataEntryResponsesList = new ArrayList<>();
            for (int i = 0; i < dataEntryResponsesList.size(); i++){

                String indicator = dataEntryResponsesList.get(i).getIndicator();
                String response = dataEntryResponsesList.get(i).getResponse();
                String comment = dataEntryResponsesList.get(i).getComment();
                String attachment = dataEntryResponsesList.get(i).getAttachment();

                DbDataEntryResponses dbDataEntryResponses = new DbDataEntryResponses(
                        indicator, response, comment, attachment);
                dbDataEntryResponsesList.add(dbDataEntryResponses);

            }


            DbDataEntryData dbDataEntryData = new DbDataEntryData(
                    dataEntry.getSelectedPeriod(),
                    dataEntry.getStatus(),
                    dataEntry.getDataEntryPersonId(),
                    dataEntry.getDataEntryDate(),
                    dbDataEntryResponsesList
            );

            return new Results(200, dbDataEntryData);

        }

        return new Results(400, "There were no records with that id.");
    }

    @Override
    public Results updateDataEntry(Long id, DbDataEntryData dbDataEntryData) {

        /**
         * TODO: Update this method
         */

        return null;
    }

    @Override
    public Results deleteDataEntry(Long id) {
        Optional<DataEntry> dataEntryOptional = dataEntryRepo.findById(id);
        if (dataEntryOptional.isPresent()){
            DataEntry dataEntry = dataEntryOptional.get();
            //Also Delete the responses
            dataEntryResponsesService.deleteDataEntryResponses(id);

            dataEntryRepo.deleteById(dataEntry.getId());


            return new Results(200, new DbDetails("Record has been deleted successfully."));

        }

        return new Results(400, "There were no records with that id.");
    }
}
