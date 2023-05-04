package agard.spring.restmvc.repositories;

import agard.spring.restmvc.domain.Beer;
import agard.spring.restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My Test Beer")
                .beerStyle(BeerStyle.LAGER)
                .upc("12345678")
                .price(new BigDecimal("11.99"))
                .build());

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }
    @Test
    void testSaveNameTooLong() {
        assertThrows(ConstraintViolationException.class, () ->   {
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .beerName("My Test Beer My Test Beer My Test Beer My Test Beer My Test Beer My Test Beer")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("12345678")
                    .price(new BigDecimal("11.99"))
                    .build());

            beerRepository.flush();
        });
    }


}