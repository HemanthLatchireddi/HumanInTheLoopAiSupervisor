package com.frontdesk.HumanInTheLoopAISupervisor.service;

import com.frontdesk.HumanInTheLoopAISupervisor.model.HelpRequest;
import com.frontdesk.HumanInTheLoopAISupervisor.repository.HelpRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HelpRequestService {
    private final HelpRequestRepository repo;

    public HelpRequestService(HelpRequestRepository repo) {
        this.repo = repo;
    }

    public HelpRequest create(String question) {
        HelpRequest r = new HelpRequest(question);
        r.setCreatedAt(LocalDateTime.now());
        r.setStatus("PENDING");
        return repo.save(r);
    }

    public List<HelpRequest> getPending() {
        return repo.findByStatusOrderByCreatedAtAsc("PENDING");
    }

    public List<HelpRequest> getAll() {
        return repo.findAll();
    }

    public HelpRequest resolve(Long id, String answer) {
        HelpRequest r = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        r.setSupervisorAnswer(answer);
        r.setStatus("RESOLVED");
        return repo.save(r);
    }
}
