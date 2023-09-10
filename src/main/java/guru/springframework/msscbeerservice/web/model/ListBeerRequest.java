package guru.springframework.msscbeerservice.web.model;

import lombok.Data;


@Data
public class ListBeerRequest {
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private Integer pageNumber;
    private Integer pageSize;
    private String beerName;
    private String beerStyle;
    private boolean showInventory;

    public ListBeerRequest(
            Integer pageNumber,
            Integer pageSize,
            String beerName,
            BeerStyleEnum beerStyleEnum,
            boolean showInventory
    ) {
        this.pageNumber = pageNumber == null || pageNumber < 0 ? DEFAULT_PAGE_NUMBER : pageNumber;
        this.pageSize = pageSize == null || pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
        this.beerName = beerName;
        this.beerStyle = beerStyle != null ? beerStyleEnum.toString() : "";
        this.showInventory = showInventory;
    }
}
