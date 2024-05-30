package io.github.meritepk.webapp.chat;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChatEtlPipeline {

    private final VectorStore vectorStore;
    private final Resource documentSource;

    public ChatEtlPipeline(VectorStore vectorStore, @Value("classpath:bikes.json") Resource documentSource) {
        this.vectorStore = vectorStore;
        this.documentSource = documentSource;
    }

    @Scheduled(fixedDelay = Long.MAX_VALUE)
    public void injest() {
        JsonReader jsonReader = new JsonReader(documentSource, "name", "price", "shortDescription", "tags");
        List<Document> documents = jsonReader.get();
        vectorStore.add(documents);
    }
}
