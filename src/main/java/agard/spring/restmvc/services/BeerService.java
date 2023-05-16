package agard.spring.restmvc.services;

import agard.spring.restmvc.model.BeerDTO;
import agard.spring.restmvc.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> getBeerList(String beerName, BeerStyle beerStyle, Boolean showInventory);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);

    Boolean deleteById(UUID beerID);

    Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer);
}
