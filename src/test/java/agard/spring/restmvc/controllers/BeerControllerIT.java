package agard.spring.restmvc.controllers;

import agard.spring.restmvc.domain.Beer;
import agard.spring.restmvc.mappers.BeerMapper;
import agard.spring.restmvc.model.BeerDTO;
import agard.spring.restmvc.model.BeerStyle;
import agard.spring.restmvc.repositories.BeerRepository;
import agard.spring.restmvc.services.BeerServiceJPA;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.*;

import static agard.spring.restmvc.controllers.BeerController.BEER_PATH;
import static agard.spring.restmvc.controllers.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity()).build();
    }

    @Test
    void testListBeers() {
        Page<BeerDTO> dtos = beerController.beersList(null, null, false, 1, 1200);

        assertThat(dtos.getContent().size()).isEqualTo(25);
    }

    @Test
    void testListByName() throws Exception {
        mockMvc.perform(get(BEER_PATH)
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("pageSize", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(336)));

    }

    @Test
    void testListByStyle() throws Exception{
        mockMvc.perform(get(BEER_PATH)
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()",is(548)));
    }

    @Test
    void testListByNameAndStyle() throws Exception {
        mockMvc.perform(get(BEER_PATH)
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(310)));
    }

    @Test
    void testListByNameAndStyleShowInventory() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(BEER_PATH)
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageSize", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testListByNameAndStyleShowInventoryFalse() throws Exception {
        mockMvc.perform(get(BEER_PATH)
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false")
                        .queryParam("pageSize", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void testListByNameAndStyleShowInventoryPaging() throws Exception {
        mockMvc.perform(get(BEER_PATH)
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize","50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(50)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.first", is(false)))
                .andExpect(jsonPath("$.number", is(1)));
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        Page<BeerDTO> dtos = beerController.beersList(null, null, false, 1, 25);

        assertThat(dtos.getContent().size()).isEqualTo(0);
    }

    @Test
    void testGetById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO dto = beerController.getBeerById(beer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            BeerDTO dto = beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("My New Beer")
                .build();

        ResponseEntity responseEntity = beerController.handlePost(beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);


        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
    }


    @Rollback
    @Transactional
    @Test
    void testUpdateById() {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO dto = beerMapper.beerToBeerDto(beer);
        dto.setId(null);
        dto.setVersion(null);
        final String beerName = "Updated Name";
        dto.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateById(beer.getId(), dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteById() {
        Beer beer = beerRepository.findAll().get(0);

        ResponseEntity  responseEntity = beerController.deleteById(beer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }

    @Test
    void testDeleteNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testPatchById() {
        Beer beer = beerRepository.findAll().get(0);

        final String oldUpc = beer.getUpc();

        BeerDTO dto =  beerMapper.beerToBeerDto(beer);
        dto.setId(null);
        dto.setVersion(null);
        dto.setBeerStyle(null);
        final String beerPatch = "Patched Name";
        dto.setBeerName(beerPatch);
        BigDecimal pricePatch = new BigDecimal(15.99);
        dto.setPrice(pricePatch);
        dto.setUpc(null);
        dto.setQuantityOnHand(null);

        ResponseEntity responseEntity = beerController.patchById(beer.getId(), dto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer patchedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(patchedBeer.getBeerName()).isEqualTo(beerPatch);
        assertThat(patchedBeer.getPrice()).isEqualTo(pricePatch);
        assertThat(patchedBeer.getUpc()).isEqualTo(oldUpc);
    }

    @Test
    void testPatchNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.patchById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Test
    void testPatchBadName() throws Exception {
        Beer beer = beerRepository.findAll().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name New Name New Name New Name New Name New Name New Name");

        MvcResult mockMvcResult = mockMvc.perform(patch(BEER_PATH_ID, beer.getId())
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(mockMvcResult.getResponse().getContentAsString());
    }

    //Security //
    @Test
    void testUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get(BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


}