package guru.springframework.msscbeerservice.web.controller;

import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.ListBeerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("api/v1/beer")
@RestController
public class BeerController {

    private final BeerService beerService;

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId, @RequestParam(value = "showInventory", required = false) boolean showInventory) {
        return new ResponseEntity<>(beerService.getBeerById(beerId, showInventory), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BeerDto> saveNewBeer(@RequestBody @Validated BeerDto beerDto) {
        return new ResponseEntity(beerService.saveNewBeer(beerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> updateBeer(@PathVariable("beerId") UUID beerId, @RequestBody @Validated BeerDto beerDto) {
        return new ResponseEntity(beerService.updateBeer(beerId, beerDto), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/list", produces = {"application/json"})
    public ResponseEntity<BeerPagedList> listBeers(@RequestBody ListBeerRequest listBeerRequest) {
        BeerPagedList beerPagedList =
                beerService.listBeers(
                        listBeerRequest.getBeerName(),
                        listBeerRequest.getBeerStyle(),
                        PageRequest.of(
                                listBeerRequest.getPageNumber(),
                                listBeerRequest.getPageSize()
                        ),
                        listBeerRequest.isShowInventory());
        return new ResponseEntity<>(beerPagedList, HttpStatus.OK);
    }
}
