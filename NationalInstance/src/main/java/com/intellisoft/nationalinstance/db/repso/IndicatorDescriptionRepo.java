package com.intellisoft.nationalinstance.db.repso;

import com.intellisoft.nationalinstance.db.IndicatorDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;

public interface IndicatorDescriptionRepo extends JpaRepository<IndicatorDescription, Long> {
    Optional<IndicatorDescription> findByCode(String code);
}
