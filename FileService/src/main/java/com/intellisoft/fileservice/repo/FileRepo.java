package com.intellisoft.fileservice.repo;

import com.intellisoft.fileservice.model.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepo extends JpaRepository<Files, String> {
}