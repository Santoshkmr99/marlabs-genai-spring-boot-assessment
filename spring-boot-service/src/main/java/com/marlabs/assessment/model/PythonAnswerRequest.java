package com.marlabs.assessment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PythonAnswerRequest(
        @JsonProperty("caller_id")
        String callerId,

        String tenant,

        String role,

        String question,

        @JsonProperty("as_of")
        String asOf
) {
}