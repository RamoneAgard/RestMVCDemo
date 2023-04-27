package agard.spring.restmvc.services;

import agard.spring.restmvc.model.Beer;
import agard.spring.restmvc.model.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<Beer> getBeerList();
    Optional<Beer> getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);
    void updateBeerById(UUID beerId, Beer beer);

    void deleteById(UUID beerID);

    void patchBeerById(UUID beerId, Beer beer);
}
