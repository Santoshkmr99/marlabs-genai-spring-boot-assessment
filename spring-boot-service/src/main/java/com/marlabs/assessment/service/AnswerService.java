package com.marlabs.assessment.service;

import com.marlabs.assessment.model.AnswerRequest;
import com.marlabs.assessment.model.AnswerResponse;
import com.marlabs.assessment.model.Caller;
import com.marlabs.assessment.model.Citation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    private final CallerService callerService;

    public AnswerService(CallerService callerService) {
        this.callerService = callerService;
    }

    public AnswerResponse answer(
            String callerId,
            AnswerRequest request) {

        Caller caller = callerService.resolveCaller(callerId);

        // Temporary implementation.
        // Python integration will replace this.
        return new AnswerResponse(
                "ANSWERED",
                "Caller resolved successfully for "
                        + caller.tenant()
                        + " / "
                        + caller.role(),
                List.of(
                        new Citation(
                                "temporary",
                                "Temporary response while Python service is being implemented."
                        )
                )
        );
    }
}