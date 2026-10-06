package com.marlabs.assessment.model;

public record Caller(
        String callerId,
        String tenant,
        String role
) {
}
