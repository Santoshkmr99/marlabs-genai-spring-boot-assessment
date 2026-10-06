package com.marlabs.assessment.service;

import com.marlabs.assessment.exception.UnknownCallerException;
import com.marlabs.assessment.model.Caller;
import com.marlabs.assessment.repository.CallerRepository;
import org.springframework.stereotype.Service;

@Service
public class CallerService {

    private final CallerRepository callerRepository;

    public CallerService(CallerRepository callerRepository) {
        this.callerRepository = callerRepository;
    }

    public Caller resolveCaller(String callerId) {

        if (callerId == null || callerId.isBlank()) {
            throw new UnknownCallerException("missing");
        }

        return callerRepository.findByCallerId(callerId)
                .orElseThrow(() -> new UnknownCallerException(callerId));
    }
}