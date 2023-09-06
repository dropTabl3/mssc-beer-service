package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.controller.BusinessException;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;

    private final BeerMapper beerMapper;

    @Override
    public BeerDto getBeerById(UUID beerId) {
        return beerMapper.beerToBeerDto(beerRepository.findById(beerId).orElseThrow(() -> new BusinessException("Beer not found")));
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        beerDto.setId(beerId);
        return beerMapper.beerToBeerDto(beerRepository.saveAndFlush(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    public BeerPagedList listBeers(String beerName, String beerStyle, PageRequest pageRequest) {
        BeerPagedList beerPagedList;
        Page<Beer> beerPage = evaluateSearchFilter(beerName, beerStyle, pageRequest);
        List<BeerDto> beerDtoList = beerPage.getContent().stream().map(beerMapper::beerToBeerDto).collect(Collectors.toList());
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
}
