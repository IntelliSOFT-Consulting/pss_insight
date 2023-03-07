package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.DbDataEntryResponses;
import com.intellisoft.nationalinstance.DbDetails;
import com.intellisoft.nationalinstance.Results;
import com.intellisoft.nationalinstance.db.DataEntryResponses;
import com.intellisoft.nationalinstance.db.repso.DataEntryResponsesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataEntryResponsesServiceImpl implements DataEntryResponsesService{

    private final DataEntryResponsesRepo dataEntryResponsesRepo;

    @Override
    public void addDataEntryResponses(List<DataEntryResponses> dataEntryResponsesList) {
        dataEntryResponsesRepo.saveAll(dataEntryResponsesList);
    }

    @Override
    public List<DataEntryResponses> listDataEntryResponses(Long dataEntryId) {
        return dataEntryResponsesRepo.findAllByDataEntryId(dataEntryId);
    }

    @Override
    public DataEntryResponses updateDataEntryResponses(Long id, DbDataEntryResponses dataEntryResponses) {

        return dataEntryResponsesRepo.findById(id)
                .map(dataEntryResponsesOld ->{
                    dataEntryResponsesOld.setAttachment(dataEntryResponses.getAttachment());
                    dataEntryResponsesOld.setComment(dataEntryResponses.getComment());
                    dataEntryResponsesOld.setResponse(dataEntryResponses.getResponse());
                    return dataEntryResponsesRepo.save(dataEntryResponsesOld);
                } ).orElse(null);

    }

    @Override
    public void deleteDataEntryResponses(Long id) {

        Optional<DataEntryResponses> optionalDataEntryResponses =
                dataEntryResponsesRepo.findById(id);
        if (optionalDataEntryResponses.isPresent()){
            dataEntryResponsesRepo.deleteById(id);
            new Results(200, new DbDetails("Deleted successfully."));
            return;
        }

        new Results(400, "Record not found.");
    }
}
