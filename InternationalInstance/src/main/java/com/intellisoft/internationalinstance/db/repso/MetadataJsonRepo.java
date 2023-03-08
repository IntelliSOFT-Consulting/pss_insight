package com.intellisoft.internationalinstance.db.repso;

import com.intellisoft.internationalinstance.db.MetadataJson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetadataJsonRepo extends JpaRepository<MetadataJson, String> {

    Optional<MetadataJson> findByCode(String code);
}
