package com.intellisoft.nationalinstance.db.repso;

import com.intellisoft.nationalinstance.db.DataEntry;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataEntryRepo extends JpaRepository<DataEntry, Long> {
    Page<DataEntry> findAllByStatusAndDataEntryPersonId(String status, String dataEntryPersonId, Pageable pageable);
}
