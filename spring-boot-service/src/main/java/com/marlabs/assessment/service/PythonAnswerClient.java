package com.marlabs.assessment.service;

import tools.jackson.databind.ObjectMapper;

import com.marlabs.assessment.model.AnswerRequest;
import com.marlabs.assessment.model.AnswerResponse;
import com.marlabs.assessment.model.Caller;
import com.marlabs.assessment.model.PythonAnswerRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class PythonAnswerClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String pythonServiceUrl;

    public PythonAnswerClient(
            ObjectMapper objectMapper,
            @Value("${python.service.url}") String pythonServiceUrl) {

        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
        this.pythonServiceUrl = pythonServiceUrl;
    }

    public AnswerResponse getAnswer(
            Caller caller,
            AnswerRequest request)
            throws IOException, InterruptedException {

        PythonAnswerRequest pythonRequest =
                new PythonAnswerRequest(
                        caller.callerId(),
                        caller.tenant(),
                        caller.role(),
                        request.question(),
                        request.asOf()
                );

        String requestJson =
                objectMapper.writeValueAsString(pythonRequest);

        byte[] requestBody =
                requestJson.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(
                        pythonServiceUrl + "/internal/answer"
                ))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                .build();

        HttpResponse<String> response =
                httpClient.send(
                        httpRequest,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Python service returned HTTP "
                            + response.statusCode()
                            + ": "
                            + response.body()
            );
        }

        return objectMapper.readValue(
                response.body(),
                AnswerResponse.class
        );
    }
}