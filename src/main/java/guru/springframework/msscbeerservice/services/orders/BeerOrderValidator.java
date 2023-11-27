package guru.springframework.msscbeerservice.services.orders;

import guru.sfg.common.events.BeerOrderDto;
import guru.sfg.common.events.BeerOrderLineDto;
import guru.sfg.common.events.ValidateBeerOrderRequest;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Component
public class BeerOrderValidator {

    private final BeerRepository beerRepository;

    public boolean validate(ValidateBeerOrderRequest request) {
        BeerOrderDto beerOrderDto = request.getBeerOrderDto();
        boolean isValid = true;
        if(beerOrderDto != null && CollectionUtils.isEmpty(beerOrderDto.getBeerOrderLines())){
            isValid = false;
        }
        for (BeerOrderLineDto line : beerOrderDto.getBeerOrderLines()) {
            if(beerRepository.findOneByUpc(line.getUpc()) == null) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

}
