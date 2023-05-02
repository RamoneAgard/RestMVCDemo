package agard.spring.restmvc.services;

import agard.spring.restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> getBeerList();
    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);
    void updateBeerById(UUID beerId, BeerDTO beer);

    void deleteById(UUID beerID);

    void patchBeerById(UUID beerId, BeerDTO beer);
}
