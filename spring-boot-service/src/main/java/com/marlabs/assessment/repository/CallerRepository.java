package com.marlabs.assessment.repository;

import com.marlabs.assessment.model.Caller;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class CallerRepository {

    private final Map<String, Caller> callers = Map.of(
            "atlas-employee-01",
            new Caller(
                    "atlas-employee-01",
                    "Atlas",
                    "employee"
            ),

            "atlas-contractor-01",
            new Caller(
                    "atlas-contractor-01",
                    "Atlas",
                    "contractor"
            ),

            "boreal-employee-01",
            new Caller(
                    "boreal-employee-01",
                    "Boreal",
                    "employee"
            )
    );

    public Optional<Caller> findByCallerId(String callerId) {
        return Optional.ofNullable(callers.get(callerId));
    }
}