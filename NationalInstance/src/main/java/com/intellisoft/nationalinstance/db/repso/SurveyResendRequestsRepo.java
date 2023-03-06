package com.intellisoft.nationalinstance.db.repso;

import com.intellisoft.nationalinstance.db.SurveyResendRequests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyResendRequestsRepo extends JpaRepository<SurveyResendRequests, Long> {
}
