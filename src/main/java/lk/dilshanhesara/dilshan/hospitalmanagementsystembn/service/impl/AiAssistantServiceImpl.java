package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Map;

@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // For safe JSON parsing

    @Override
    public String getChatResponse(String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String fullUrl = GEMINI_API_URL + "?key=" + apiKey;

        String systemInstruction = "You are a helpful AI health assistant for HMS. Provide general health advice. " +
                "Do NOT provide a diagnosis. Always advise the user to consult a real doctor. " +
                "IMPORTANT: Format your response using simple HTML. Use <b>...</b> tags for headings. Use <ul> and <li> for lists.";

        Map<String, Object> body = Map.of(
                "contents", Collections.singletonList(
                        Map.of("parts", Collections.singletonList(
                                Map.of("text", systemInstruction + "\n\nUser: " + userMessage)
                        ))
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);


        try {
            String jsonResponse = restTemplate.postForObject(fullUrl, entity, String.class);


            // --- CRITICAL FIX: Safely parse the JSON to get the text ---
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I am unable to process your request at the moment.";
        }
    }


}