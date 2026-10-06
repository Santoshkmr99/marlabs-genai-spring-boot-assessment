package com.marlabs.assessment.service;

import com.marlabs.assessment.model.AnswerRequest;
import com.marlabs.assessment.model.AnswerResponse;
import com.marlabs.assessment.model.Caller;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AnswerService {

    private final CallerService callerService;
    private final PythonAnswerClient pythonAnswerClient;

    public AnswerService(
            CallerService callerService,
            PythonAnswerClient pythonAnswerClient) {

        this.callerService = callerService;
        this.pythonAnswerClient = pythonAnswerClient;
    }

    public AnswerResponse answer(
            String callerId,
            AnswerRequest request) {

        Caller caller = callerService.resolveCaller(callerId);

        try {
            return pythonAnswerClient.getAnswer(
                    caller,
                    request
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Python service call was interrupted",
                    e
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to communicate with Python service",
                    e
            );
        }
    }
}