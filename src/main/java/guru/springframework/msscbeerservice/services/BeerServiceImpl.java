package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.controller.BusinessException;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;

    private final BeerMapper beerMapper;

    //The logic behind condition is saying that we only get values from cache if we don't need to call inventory
    //Otherwise, showInventory == true means that cache is never used and inventory is always called
    @Cacheable(cacheNames = "beerListCache", condition = "#showInventory == false")
    @Override
    //TODO refactor and isolate APIS for every param
    public BeerPagedList listBeers(String beerName, String beerStyle, PageRequest pageRequest, boolean showInventory) {
        System.out.println("beerListCache : DB is called");

        BeerPagedList beerPagedList;

        Page<Beer> beerPage = evaluateSearchFilter(beerName, beerStyle, pageRequest);

        List<BeerDto> beerDtoList = beerPage.getContent()
                .stream()
                .map(showInventory ? beerMapper::beerToBeerDtoWithInventory : beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
        PageRequest beerPageRequest = PageRequest.of(beerPage.getPageable().getPageNumber(), beerPage.getPageable().getPageSize());

        beerPagedList = new BeerPagedList(beerDtoList, beerPageRequest, beerPage.getTotalElements());
        return beerPagedList;
    }

    private Page<Beer> evaluateSearchFilter(String beerName, String beerStyle, PageRequest pageRequest) {
        Page<Beer> beerPage;
        if (StringUtils.isNotEmpty(beerName) && StringUtils.isNotEmpty(beerStyle)) {
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if (StringUtils.isNotEmpty(beerName)) {
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (StringUtils.isNotEmpty(beerStyle)) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }
        return beerPage;
    }

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventory == false")
    @Override
    public BeerDto getBeerById(UUID beerId, boolean showInventory) {
        System.out.println("beerCache : DB is called");

        if (showInventory) {
            return beerMapper.beerToBeerDtoWithInventory(beerRepository.findById(beerId).orElseThrow(() -> new BusinessException("Beer not found")));
        } else {
            return beerMapper.beerToBeerDto(beerRepository.findById(beerId).orElseThrow(() -> new BusinessException("Beer not found")));
        }
    }

    @Cacheable(cacheNames = "beerUpcCache", condition = "#showInventory == false")
    @Override
    public BeerDto getBeerByUpc(String upc, boolean showInventory) {
        System.out.println("beerCache : DB is called");

        BeerDto beerDto;
        if (showInventory) {
            beerDto = beerMapper.beerToBeerDtoWithInventory(beerRepository.findOneByUpc(upc));
        } else {
            beerDto = beerMapper.beerToBeerDto(beerRepository.findOneByUpc(upc));
        }
        return beerDto;
    }


    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDtoWithInventory(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        beerDto.setId(beerId);
        return beerMapper.beerToBeerDtoWithInventory(beerRepository.saveAndFlush(beerMapper.beerDtoToBeer(beerDto)));
    }

}
