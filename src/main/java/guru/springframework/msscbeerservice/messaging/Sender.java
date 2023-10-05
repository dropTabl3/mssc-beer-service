package guru.springframework.msscbeerservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.config.JMSConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class Sender {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage(){
        HelloMessage message = HelloMessage.builder()
            .id(UUID.randomUUID())
            .message("Hello world")
            .build();

        jmsTemplate.convertAndSend(JMSConfig.MY_QUEUE, message);

    }
}
