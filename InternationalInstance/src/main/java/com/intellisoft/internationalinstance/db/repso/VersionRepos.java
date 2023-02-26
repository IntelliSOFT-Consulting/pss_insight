package com.intellisoft.internationalinstance.db.repso;

import com.intellisoft.internationalinstance.db.VersionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VersionRepos extends CrudRepository<VersionEntity, Long> {
    @Override
    Optional<VersionEntity> findById(Long aLong);

    Optional<VersionEntity> findByVersionName(String versionName);
}
