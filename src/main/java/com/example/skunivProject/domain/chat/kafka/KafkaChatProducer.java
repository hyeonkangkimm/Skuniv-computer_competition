package com.example.skunivProject.domain.chat.kafka;

import com.example.skunivProject.domain.chat.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//producer
@Service
@RequiredArgsConstructor
public class KafkaChatProducer {

    private final KafkaTemplate<String, RequestDto.ChatMessageDto> kafkaTemplate;

    public void sendChatMessage(RequestDto.ChatMessageDto message) {
        kafkaTemplate.send("chat-topic", message);
    }
}
