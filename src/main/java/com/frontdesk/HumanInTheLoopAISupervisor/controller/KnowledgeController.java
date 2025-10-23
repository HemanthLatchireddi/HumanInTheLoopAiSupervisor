package com.frontdesk.HumanInTheLoopAISupervisor.controller;

import com.frontdesk.HumanInTheLoopAISupervisor.model.Knowledge;
import com.frontdesk.HumanInTheLoopAISupervisor.service.KnowledgeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
@CrossOrigin(origins = "*")
public class KnowledgeController {
    private final KnowledgeService service;

    public KnowledgeController(KnowledgeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Knowledge> all() {
        return service.all();
    }
}
