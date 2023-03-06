package com.intellisoft.internationalinstance.db.repso;

import com.intellisoft.internationalinstance.db.VersionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VersionRepos extends CrudRepository<VersionEntity, Long> {
    @Override
    Optional<VersionEntity> findById(Long aLong);

    Optional<VersionEntity> findByVersionName(String versionName);

    @Query("SELECT v.id FROM VersionEntity v ORDER BY v.id DESC")
    Long findLatestId();

    @Modifying
    @Query("UPDATE VersionEntity v SET v.status = :status")
    void updateAllIsPublishedToFalse(@Param("status") String status);

    Page<VersionEntity> findAllByStatus(String status, Pageable pageable);
}
