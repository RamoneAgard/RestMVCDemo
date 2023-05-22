package agard.spring.restmvc.repositories;

import agard.spring.restmvc.domain.Beer;
import agard.spring.restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    Page<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName, Pageable pageable);

    Page<Beer> findAllByBeerStyle(BeerStyle style, Pageable pageable);

    Page<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle style, Pageable pageable);
}
