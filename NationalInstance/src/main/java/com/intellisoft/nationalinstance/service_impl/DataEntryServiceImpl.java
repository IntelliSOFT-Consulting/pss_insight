package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.*;
import com.intellisoft.nationalinstance.db.DataEntry;
import com.intellisoft.nationalinstance.db.DataEntryResponses;
import com.intellisoft.nationalinstance.db.MetadataJson;
import com.intellisoft.nationalinstance.db.repso.DataEntryRepo;
import com.intellisoft.nationalinstance.exception.CustomException;
import com.intellisoft.nationalinstance.model.OutgoingAnswers;
import com.intellisoft.nationalinstance.model.Response;
import com.intellisoft.nationalinstance.util.AppConstants;
import com.intellisoft.nationalinstance.util.GenericWebclient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;
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
    private final MetadataJsonService metadataJsonService;

    @Override
    public Results addDataEntry(DbDataEntryData dbDataEntryData) throws URISyntaxException {

        String selectedPeriod = dbDataEntryData.getSelectedPeriod();
        String status = dbDataEntryData.getStatus();
        String dataEntryPersonId = dbDataEntryData.getDataEntryPersonId();
        String dateEntryDate = getEntryDate();
        String orgUnit = dbDataEntryData.getOrgUnit();

        DataEntry dataEntry = new DataEntry();
        dataEntry.setStatus(status);
        dataEntry.setSelectedPeriod(selectedPeriod);
        dataEntry.setDataEntryPersonId(dataEntryPersonId);
        dataEntry.setDataEntryDate(dateEntryDate);
        DataEntry dataEntryAdded = dataEntryRepo.save(dataEntry);

        Long dataEntryId = dataEntryAdded.getId();
        List<DbDataEntryResponses> responsesList = dbDataEntryData.getResponses();


        //Publish data
        /**
         * The provided indicator id is the data point,
         * therefore, get the comments and uploads ids from db
         */

        List<DbDataValues> dbDataValuesList = new ArrayList<>();
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

            //Check if response is boolean
            String responseData = "";
            if (response != null ){

                if (response.contains("true")){
                    responseData = "1";
                }else if (response.contains("false")){
                    responseData = "0";
                }else {
                    responseData = response;
                }

            }

            MetadataJson metadataJson = metadataJsonService.getMetadataJson(indicator);
            if (metadataJson != null){
                String code = metadataJson.getCode();
                String metadataDataPoint = metadataJson.getMetadata();

                if (comment != null){
                    String codeComment = code+"_Comments";
                    MetadataJson metadataJsonComment = metadataJsonService.getMetadataJsonByCode(codeComment);
                    if (metadataJsonComment != null){
                        String commentId = metadataJsonComment.getId();
                        DbDataValues dbDataValues = new DbDataValues(commentId, comment);
                        dbDataValuesList.add(dbDataValues);
                    }
                }

                if (attachment != null){
                    String codeUploads = code+"_Uploads";
                    MetadataJson metadataJsonUploads = metadataJsonService.getMetadataJsonByCode(codeUploads);
                    if (metadataJsonUploads != null){
                        String uploadId = metadataJsonUploads.getId();
                        DbDataValues dbDataValues = new DbDataValues(uploadId, attachment);
                        dbDataValuesList.add(dbDataValues);
                    }
                }

            }

            DbDataValues dbDataValues = new DbDataValues(indicator, responseData);
            dbDataValuesList.add(dbDataValues);

        }

        if (status != null && status.equals(PublishStatus.PUBLISHED.name())){

            DbDataEntry dbDataEntry = new DbDataEntry(
                    "T4EBleGG9mU",
                    orgUnit,
                    selectedPeriod + "01"+"01",
                    "COMPLETED",
                    dataEntryPersonId,
                    dbDataValuesList);

            var response = GenericWebclient.postForSingleObjResponse(
                    AppConstants.EVENTS_ENDPOINT,
                    dbDataEntry,
                    DbDataEntry.class,
                    String.class);


            System.out.println("RESPONSE FROM REMOTE: {}"+response);


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
                    "",
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
