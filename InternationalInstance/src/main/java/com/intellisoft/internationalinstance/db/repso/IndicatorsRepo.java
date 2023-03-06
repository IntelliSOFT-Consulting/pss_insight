package com.intellisoft.internationalinstance.db.repso;

import com.intellisoft.internationalinstance.db.Indicators;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicatorsRepo extends CrudRepository<Indicators, Long> {
    List<Indicators> findAll();
    @Query("SELECT i.metadata FROM Indicators i WHERE i.indicatorId in :indicatorIds")
    List<String> findByIndicatorIds(List<String> indicatorIds);

    Boolean existsByIndicatorId(String indicatorId);
}
