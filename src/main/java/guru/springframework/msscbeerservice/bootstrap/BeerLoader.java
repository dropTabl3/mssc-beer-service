package guru.springframework.msscbeerservice.bootstrap;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import org.springframework.boot.CommandLineRunner;

import java.math.BigDecimal;

public class BeerLoader implements CommandLineRunner {
    private BeerRepository beerRepository;

    public BeerLoader(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBeerObjects();
    }

    private void loadBeerObjects() {
        if (beerRepository.count() == 0) {
            beerRepository.save(Beer.builder()
                    .beerName("Porky Dee")
                    .beerStyle("IPA")
                    .upc(200000000L)
                    .price(new BigDecimal("12.95"))
                    .minOnHand(12)
                    .build());
            beerRepository.save(Beer.builder()
                    .beerName("Porky Gee")
                    .beerStyle("IPA")
                    .upc(2000000L)
                    .price(new BigDecimal("10.95"))
                    .minOnHand(10)
                    .build());
        }
    }
}
