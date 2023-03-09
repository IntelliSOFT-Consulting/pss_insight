package com.intellisoft.internationalinstance.db.repso;

import com.intellisoft.internationalinstance.db.Indicators;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicatorsRepo extends CrudRepository<Indicators, Long> {
    List<Indicators> findAll();
    @Query("SELECT i.metadata FROM Indicators i WHERE i.indicatorId in :indicators")
    public abstract List<String> findByIndicatorIds(@Param("indicators") List<String> indicators);


    Boolean existsByIndicatorId(String indicatorId);
}
