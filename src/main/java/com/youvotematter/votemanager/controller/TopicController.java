package com.youvotematter.votemanager.controller;

import com.youvotematter.votemanager.service.TopicService;
import com.youvotematter.votemanager.service.vos.TopicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;
    @GetMapping
    public ResponseEntity getAll() {
        return ResponseEntity.of(topicService.listAll());
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TopicVO topicVO) {
        return ResponseEntity.of(topicService.create(topicVO));
    }
}
