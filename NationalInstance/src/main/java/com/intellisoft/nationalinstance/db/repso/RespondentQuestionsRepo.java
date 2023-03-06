package com.intellisoft.nationalinstance.db.repso;

import com.intellisoft.nationalinstance.db.RespondentQuestions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespondentQuestionsRepo extends CrudRepository<RespondentQuestions,Long> {
}
