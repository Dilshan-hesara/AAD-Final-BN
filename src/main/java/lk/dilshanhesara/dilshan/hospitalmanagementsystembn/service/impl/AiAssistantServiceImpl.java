package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    private final String projectId;
    private final String location;
    private final String modelName = "gemini-1.5-flash-001"; // Or another suitable Gemini model

    public AiAssistantServiceImpl(
            @Value("${gemini.project.id}") String projectId,
            @Value("${gemini.location}") String location) {
        this.projectId = projectId;
        this.location = location;
    }

    @Override
    public String getChatResponse(String userMessage) {
        try (VertexAI vertexAi = new VertexAI(this.projectId, this.location)) {
            GenerativeModel model = new GenerativeModel(this.modelName, vertexAi);

            // Create a chat session with a system prompt
            ChatSession chat = new ChatSession(model);
            chat.sendMessage("System Instruction: You are a helpful AI health assistant for HMS. You provide general health advice and information based on user-described symptoms. You must never provide a medical diagnosis. Always end your response by strongly recommending the user to consult a real doctor for a proper diagnosis.");

            // Send the user's message and get the response
            GenerateContentResponse response = chat.sendMessage(userMessage);
            return ResponseHandler.getText(response);
        } catch (IOException e) {
            e.printStackTrace();
            return "Sorry, I am unable to process your request at the moment.";
        }
    }

    }