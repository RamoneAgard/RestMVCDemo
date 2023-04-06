package agard.spring.restmvc.services;

import agard.spring.restmvc.model.Beer;
import agard.spring.restmvc.model.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BeerService {

    List<Beer> getBeerList();
    Beer getBeerById(UUID id);

}
