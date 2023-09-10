package guru.springframework.msscbeerservice.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.msscbeerservice.web.controller.BeerController;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import guru.springframework.msscbeerservice.web.model.ListBeerRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String beerDtoToJson;

    String validListBeerToJson;

    @MockBean
    BeerService beerService;

    @MockBean
    BeerInventoryService beerInventoryService;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        BeerDto validBeer = BeerDto.builder()
                .beerName("Porky Dee")
                .beerStyle(BeerStyleEnum.GOSE.name())
                .upc("0631234200036")
                .price(new BigDecimal("13.30"))
                .build();

        ListBeerRequest validListBeer = new ListBeerRequest(0, 0, "Mango Bobs", BeerStyleEnum.IPA, true);

        beerDtoToJson = objectMapper.writeValueAsString(validBeer);
        validListBeerToJson = objectMapper.writeValueAsString(validListBeer);
    }

    @Test
    void getBeerById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/" + UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void saveNewBeer() throws Exception {
        mockMvc.perform(post("/api/v1/beer/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoToJson))
                .andExpect(status().isCreated());
    }

    @Test
    void updateBeer() throws Exception {
        mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoToJson))
                .andExpect(status().isNoContent());
    }

}