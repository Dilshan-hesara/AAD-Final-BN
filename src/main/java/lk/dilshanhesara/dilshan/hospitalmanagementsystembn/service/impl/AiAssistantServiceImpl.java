package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    @Override
    public String getChatResponse(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String fullUrl = GEMINI_API_URL + "?key=" + apiKey;

        String systemInstruction = "You are a helpful AI health assistant for HMS. Provide general health advice and information based on user-described symptoms. Do not provide medical diagnoses. Always advise the user to consult a real doctor for serious issues.";

        // Create the JSON body for the Gemini API
        Map<String, Object> body = Map.of(
                "contents", Collections.singletonList(
                        Map.of("parts", Collections.singletonList(
                                Map.of("text", systemInstruction + "\n\nUser: " + userMessage)
                        ))
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            // Make the API call
            Map<String, Object> response = restTemplate.postForObject(fullUrl, entity, Map.class);

            // Extract the text from the complex JSON response
            return (String) ((Map<?, ?>) ((Map<?, ?>) ((java.util.List<?>) ((java.util.List<?>) response.get("candidates")).get(0)).get(Integer.parseInt("content"))).get("parts")).get("text");

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I am unable to process your request at the moment.";
        }
    }
}