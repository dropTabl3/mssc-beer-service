package guru.springframework.msscbeerservice.bootstrap;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import org.springframework.boot.CommandLineRunner;

import java.math.BigDecimal;

public class BeerLoader implements CommandLineRunner {

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";
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
                    .upc(BEER_1_UPC)
                    .price(new BigDecimal("12.95"))
                    .minOnHand(12)
                    .build());
            beerRepository.save(Beer.builder()
                    .beerName("Porky Gee")
                    .beerStyle("IPA")
                    .upc(BEER_2_UPC)
                    .price(new BigDecimal("10.95"))
                    .minOnHand(10)
                    .build());
            beerRepository.save(Beer.builder()
                    .beerName("Peroni")
                    .beerStyle("LAGER")
                    .upc(BEER_3_UPC)
                    .price(new BigDecimal("10.95"))
                    .minOnHand(10)
                    .build());
        }
    }
}
