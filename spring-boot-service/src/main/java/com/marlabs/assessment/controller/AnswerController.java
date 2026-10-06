package com.marlabs.assessment.controller;

import com.marlabs.assessment.model.AnswerRequest;
import com.marlabs.assessment.model.AnswerResponse;
import com.marlabs.assessment.service.AnswerService;
import jakarta.validation.Valid;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public ResponseEntity<AnswerResponse> answer(
            @RequestHeader("X-Caller-Id") String callerId,
            @Valid @RequestBody AnswerRequest request) throws IOException, InterruptedException {

        return ResponseEntity.ok(
                answerService.answer(callerId, request)
        );
    }
}