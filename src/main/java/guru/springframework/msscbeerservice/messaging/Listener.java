package guru.springframework.msscbeerservice.messaging;

import guru.springframework.msscbeerservice.config.JMSConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class Listener {
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.MY_QUEUE)
    public void listen(@Payload HelloMessage helloMessage, @Headers MessageHeaders headers, Message message)  {
        System.out.println(headers);
        System.out.println(helloMessage);
        System.out.println(message);
    }
}
