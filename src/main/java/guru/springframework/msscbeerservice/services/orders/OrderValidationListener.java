package guru.springframework.msscbeerservice.services.orders;

import guru.sfg.common.events.BeerOrderDto;
import guru.sfg.common.events.ValidateBeerOrderRequest;
import guru.sfg.common.events.ValidateBeerOrderResponse;
import guru.springframework.msscbeerservice.config.JMSConfig;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderValidationListener {
    private final JmsTemplate jmsTemplate;
    private final BeerRepository beerRepository;
    private final BeerOrderValidator beerOrderValidator;

    @Transactional
    @JmsListener(destination = JMSConfig.VALIDATE_ORDER_Q)
    public void listen(ValidateBeerOrderRequest request){
        BeerOrderDto beerOrderDto = request.getBeerOrderDto();
        boolean isValid = !CollectionUtils.isEmpty(beerOrderDto.getBeerOrderLines()) && beerOrderValidator.validate(request);
        ValidateBeerOrderResponse response = ValidateBeerOrderResponse.builder()
                .valid(isValid)
                .orderId(beerOrderDto.getId())
                .build();
        jmsTemplate.convertAndSend(JMSConfig.VALIDATE_ORDER_RESULT_Q, response);
    }

}
