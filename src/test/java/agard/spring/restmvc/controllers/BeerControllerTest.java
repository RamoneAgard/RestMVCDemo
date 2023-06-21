package agard.spring.restmvc.controllers;

import agard.spring.restmvc.config.SpringSecurityConfig;
import agard.spring.restmvc.model.BeerDTO;
import agard.spring.restmvc.services.BeerService;
import agard.spring.restmvc.services.BeerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static agard.spring.restmvc.controllers.BeerController.BEER_PATH;
import static agard.spring.restmvc.controllers.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
@WebMvcTest(BeerController.class)
@Import(SpringSecurityConfig.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    public static final  String USERNAME = "user1";
    public static final  String PASSWORD = "password";

    @BeforeEach
    void setUp() {
       beerServiceImpl = new BeerServiceImpl();
    }

    // endpoint testing //
    @Test
    void testCreateNewBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.getBeerList(null, null, false, 1, 25).getContent().get(0);
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.getBeerList(null, null, false, 1, 25).getContent().get(1));

        mockMvc.perform(post(BEER_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        //System.out.println(objectMapper.writeValueAsString(beer));
    }

    @Test
    void getBeerById() throws Exception {
        BeerDTO testBeer = beerServiceImpl.getBeerList(null, null, false, 1, 25).getContent().get(0);
        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));
        mockMvc.perform(get(BEER_PATH_ID, testBeer.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void testListBeer() throws Exception {
        given(beerService.getBeerList(any(), any(), any(), any(), any()))
                .willReturn(beerServiceImpl.getBeerList(null, null, null, 1, 25));

        mockMvc.perform(get(BEER_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()",is(3)));
    }

    @Test
    void testUpdateById() throws Exception {
        BeerDTO beer = beerServiceImpl.getBeerList(null, null, false, 1, 25).getContent().get(0);

        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BEER_PATH_ID, beer.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeerById(uuidArgumentCaptor.capture(), any(BeerDTO.class));

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testDeleteById() throws Exception {
        BeerDTO beer = beerServiceImpl.getBeerList(null, null, false, 1, 25).getContent().get(0);

        given(beerService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(BEER_PATH_ID, beer.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchById() throws Exception {
        BeerDTO beer = beerServiceImpl.getBeerList(null, null, false, 1, 25).getContent().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        given(beerService.patchBeerById(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(patch(BEER_PATH_ID, beer.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void getBeerNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BEER_PATH_ID, UUID.randomUUID())
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isNotFound());
    }

    // validation testing //
    @Test
    void testCreateNullBeerName() throws Exception{
        BeerDTO dto = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class)))
                .willReturn(beerServiceImpl.getBeerList(null, null, false, 1, 25).getContent().get(1));

        MvcResult mvcResult = mockMvc.perform(post(BEER_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateBlankBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.getBeerList(null, null, false, 1, 25).getContent().get(0);
        beer.setBeerName("");
        beer.setQuantityOnHand(-1);
        beer.setUpc("");
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        MvcResult mvcResult = mockMvc.perform(put(BEER_PATH_ID, beer.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(3)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    //Security Test //
    @Test
    void testUnauthenticatedRequest() throws Exception {
        given(beerService.getBeerList(any(), any(), any(), any(), any()))
                .willReturn(beerServiceImpl.getBeerList(null, null, null, 1, 25));

        mockMvc.perform(get(BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}