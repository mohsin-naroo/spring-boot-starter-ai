package io.github.meritepk.webapp.chat;

import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class ChatConfiguration {

    @Bean
    public VectorStore vectorStore(EmbeddingClient embeddingClient) {
        return new SimpleVectorStore(embeddingClient);
    }

    @Bean
    public SystemPromptTemplate systemPromptTemplate(@Value("classpath:system-template.txt") Resource systemPrompt) {
        return new SystemPromptTemplate(systemPrompt);
    }
}
