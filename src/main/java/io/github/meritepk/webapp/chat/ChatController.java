package io.github.meritepk.webapp.chat;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private static final String DEFAULT_JOKE = "Tell me a joke";
    private static final String DEFAULT_BIKE = "What bike is good for city commuting?";

    private final ChatService chatService;
    private final String chatModel;

    public ChatController(ChatService chatService, @Value("${spring.ai.ollama.chat.options.model}") String chatModel) {
        this.chatService = chatService;
        this.chatModel = chatModel;
    }

    @GetMapping("/api/v1/chat")
    public Map<String, Object> chatGet(@RequestParam(value = "prompt", defaultValue = DEFAULT_JOKE) String prompt) {
        return chat(prompt);
    }

    @PostMapping("/api/v1/chat")
    public Map<String, Object> chatPost(@RequestParam(value = "prompt", defaultValue = DEFAULT_JOKE) String prompt) {
        return chat(prompt);
    }

    private Map<String, Object> chat(String prompt) {
        LocalDateTime start = LocalDateTime.now();
        String output = chatService.chat(prompt);
        return Map.of("prompt", prompt, "output", output, "startTime", start, "finishTime", LocalDateTime.now(),
                "model", chatModel, "type", "Chat");
    }

    @GetMapping("/api/v1/chat/rag")
    public Map<String, Object> ragGet(@RequestParam(value = "prompt", defaultValue = DEFAULT_BIKE) String prompt) {
        return generate(prompt);
    }

    @PostMapping("/api/v1/chat/rag")
    public Map<String, Object> ragPost(@RequestParam(value = "prompt", defaultValue = DEFAULT_BIKE) String prompt) {
        return generate(prompt);
    }

    public Map<String, Object> generate(String prompt) {
        LocalDateTime start = LocalDateTime.now();
        String output = chatService.generate(prompt);
        return Map.of("prompt", prompt, "output", output, "startTime", start, "finishTime", LocalDateTime.now(),
                "model", chatModel, "type", "RAG");
    }
}