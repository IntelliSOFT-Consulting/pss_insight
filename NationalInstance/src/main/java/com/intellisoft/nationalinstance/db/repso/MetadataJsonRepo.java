package com.intellisoft.nationalinstance.db.repso;

import com.intellisoft.nationalinstance.db.MetadataJson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetadataJsonRepo extends JpaRepository<MetadataJson, String> {

    Optional<MetadataJson> findByCode(String code);
}
