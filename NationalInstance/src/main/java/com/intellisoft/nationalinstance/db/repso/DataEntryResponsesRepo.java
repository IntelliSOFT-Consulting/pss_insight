package com.intellisoft.nationalinstance.db.repso;

import com.intellisoft.nationalinstance.db.DataEntryResponses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataEntryResponsesRepo extends JpaRepository<DataEntryResponses, Long> {
    List<DataEntryResponses> findAllByDataEntryId(Long dataEntryId);
}
