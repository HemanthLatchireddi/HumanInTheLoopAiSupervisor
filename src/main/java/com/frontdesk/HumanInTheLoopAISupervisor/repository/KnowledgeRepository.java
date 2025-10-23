package com.frontdesk.HumanInTheLoopAISupervisor.repository;


import com.frontdesk.HumanInTheLoopAISupervisor.model.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {
    Optional<Knowledge> findByQuestionIgnoreCase(String question);
    Optional<Knowledge> findByQuestion(String question);

}
