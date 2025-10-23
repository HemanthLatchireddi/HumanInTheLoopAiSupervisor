package com.frontdesk.HumanInTheLoopAISupervisor.controller;

import com.frontdesk.HumanInTheLoopAISupervisor.model.HelpRequest;
import com.frontdesk.HumanInTheLoopAISupervisor.model.Knowledge;
import com.frontdesk.HumanInTheLoopAISupervisor.repository.HelpRequestRepository;
import com.frontdesk.HumanInTheLoopAISupervisor.repository.KnowledgeRepository;
import com.frontdesk.HumanInTheLoopAISupervisor.service.HelpRequestService;
import com.frontdesk.HumanInTheLoopAISupervisor.service.KnowledgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/help")
@CrossOrigin(origins = "*")
public class HelpRequestController {
    private final HelpRequestService helpService;
    private final KnowledgeService knowledgeService;
    private final HelpRequestRepository helpRequestRepository;
    private final KnowledgeRepository knowledgeRepository;

    public HelpRequestController(HelpRequestService helpService, KnowledgeService knowledgeService, HelpRequestRepository helpRequestRepository, KnowledgeRepository knowledgeRepository) {
        this.helpService = helpService;
        this.knowledgeService = knowledgeService;
        this.helpRequestRepository = helpRequestRepository;
        this.knowledgeRepository = knowledgeRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> ask(@RequestBody Map<String,String> body) {
        String question = body.get("question");
        if (question == null || question.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "question required"));
        }

        return knowledgeService.findAnswer(question)
                .map(ans -> ResponseEntity.ok(Map.of("status","known","answer", ans)))
                .orElseGet(() -> {
                    HelpRequest req = helpService.create(question);
                    // Simulate notifying supervisor (webhook, SMS, console)
                    System.out.println("SUPERVISOR NOTIFY: Please help answer: \"" + question + "\" (requestId=" + req.getId() + ")");
                    return ResponseEntity.ok(Map.of("status","escalated","message","Let me check with my supervisor and get back to you."));
                });
    }

    @GetMapping("/pending")
    public List<HelpRequest> pending() {
        return helpService.getPending();
    }

    @GetMapping("/all")
    public List<HelpRequest> all() {
        return helpService.getAll();
    }

    @PostMapping("/{id}/response")
    public ResponseEntity<?> respond(@PathVariable Long id, @RequestBody Map<String,String> body) {
        String answer = body.get("answer");
        if (answer == null || answer.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error","answer required"));
        }

        HelpRequest updated = helpService.resolve(id, answer);
        knowledgeService.saveOrUpdate(updated.getQuestion(), answer);

        // Simulate sending answer back to caller (console or webhook)
        System.out.println("AI -> Caller (simulated): " + answer + "  (in response to requestId=" + id + ")");

        return ResponseEntity.ok(updated);

    }
    @PostMapping("/request")
    public ResponseEntity<?> createHelpRequest(@RequestBody Map<String, String> body) {
        String question = body.get("question").trim();

        Optional<Knowledge> known = knowledgeRepository.findByQuestion(question);
        if (known.isPresent()) {
            // Auto-answer from Knowledge Base
            Map<String, String> response = new HashMap<>();
            response.put("status", "ANSWERED_FROM_KB");
            response.put("answer", known.get().getAnswer());
            return ResponseEntity.ok(response);
        }

        HelpRequest req = new HelpRequest();
        req.setQuestion(question);
        req.setStatus("PENDING");
        HelpRequest saved = helpRequestRepository.save(req);

        Map<String, String> response = new HashMap<>();
        response.put("status", "NEEDS_SUPERVISOR");
        response.put("message", "No answer found, forwarded to supervisor");

        return ResponseEntity.ok(response);
    }


}
