package com.frontdesk.HumanInTheLoopAISupervisor.service;

import com.frontdesk.HumanInTheLoopAISupervisor.model.Knowledge;
import com.frontdesk.HumanInTheLoopAISupervisor.repository.KnowledgeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KnowledgeService {
    private final KnowledgeRepository repo;

    public KnowledgeService(KnowledgeRepository repo) {
        this.repo = repo;
    }

    public Optional<String> findAnswer(String question) {
        return repo.findByQuestionIgnoreCase(question).map(Knowledge::getAnswer);
    }

    public List<Knowledge> all() {
        return repo.findAll();
    }

    @Transactional
    public Knowledge saveOrUpdate(String question, String answer) {
        Optional<Knowledge> existing = repo.findByQuestionIgnoreCase(question);
        if (existing.isPresent()) {
            Knowledge k = existing.get();
            k.setAnswer(answer);
            return repo.save(k);
        } else {
            Knowledge k = new Knowledge(question, answer);
            return repo.save(k);
        }
    }
}
