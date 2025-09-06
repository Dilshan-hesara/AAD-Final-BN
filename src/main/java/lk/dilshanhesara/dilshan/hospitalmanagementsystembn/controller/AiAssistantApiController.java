package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.ChatRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.ChatResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AiAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiAssistantApiController {

    private final AiAssistantService aiAssistantService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDto> getAiReply(@RequestBody ChatRequestDto request) {
        String reply = aiAssistantService.getChatResponse(request.getMessage());
        return ResponseEntity.ok(new ChatResponseDto(reply));
    }
}