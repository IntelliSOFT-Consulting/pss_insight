package com.intellisoft.internationalinstance.db.repso;

import com.intellisoft.internationalinstance.db.MetadataJson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataJsonRepo extends JpaRepository<MetadataJson, String> {
}
