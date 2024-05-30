package io.github.meritepk.webapp.chat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("classpath:bikes.json")
    private Resource bikesResource;

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final SystemPromptTemplate systemPromptTemplate;

    public ChatService(ChatClient chatClient, VectorStore vectorStore, SystemPromptTemplate systemPromptTemplate) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.systemPromptTemplate = systemPromptTemplate;
    }

    public String chat(String request) {
        return chatClient.call(request);
    }

    public String generate(String message) {
        logger.info("VectorStore similarity search");
        List<Document> similarDocuments = getDocumentStore().similaritySearch(message);

        Message systemMessage = getSystemMessage(similarDocuments);
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        logger.info("ChatClient request");
        ChatResponse chatResponse = chatClient.call(prompt);
        logger.info("ChatClient response");
        return chatResponse.getResult().getOutput().getContent();
    }

    private SystemPromptTemplate getSystemTemplate() {
        return systemPromptTemplate;
    }

    private VectorStore getDocumentStore() {
        return vectorStore;
    }

    private Message getSystemMessage(List<Document> similarDocuments) {
        String documents = similarDocuments.stream().map(entry -> entry.getContent()).collect(Collectors.joining("\n"));
        Message systemMessage = getSystemTemplate().createMessage(Map.of("context", documents));
        return systemMessage;
    }
}