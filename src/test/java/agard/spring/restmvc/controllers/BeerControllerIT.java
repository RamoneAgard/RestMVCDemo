package agard.spring.restmvc.controllers;

import agard.spring.restmvc.domain.Beer;
import agard.spring.restmvc.mappers.BeerMapper;
import agard.spring.restmvc.model.BeerDTO;
import agard.spring.restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Test
    void testListBeers() {
        List<BeerDTO> dtos = beerController.beersList();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.beersList();

        assertThat(dtos.size()).isEqualTo(0);
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
}