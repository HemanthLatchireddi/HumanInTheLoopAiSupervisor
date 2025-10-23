package com.frontdesk.HumanInTheLoopAISupervisor.repository;

import com.frontdesk.HumanInTheLoopAISupervisor.model.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
    List<HelpRequest> findByStatusOrderByCreatedAtAsc(String status);
}