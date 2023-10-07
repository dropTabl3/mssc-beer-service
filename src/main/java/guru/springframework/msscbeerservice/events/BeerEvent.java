package guru.springframework.msscbeerservice.events;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
@RequiredArgsConstructor
@Data
@Builder
public class BeerEvent implements Serializable {

    private static final long serialVersionUID = 4948858647209682645L;
    private final BeerDto beerDto;
}
