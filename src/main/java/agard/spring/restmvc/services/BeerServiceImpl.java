package agard.spring.restmvc.services;

import agard.spring.restmvc.model.Beer;
import agard.spring.restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        beerMap = new HashMap<>();

        Beer b1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("City Wide")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("98765")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(15)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer b2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Red Stripe")
                .beerStyle(BeerStyle.LAGER)
                .upc("45678")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(7)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer b3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Elysian Space Dust")
                .beerStyle(BeerStyle.IPA)
                .upc("12345")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(12)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(b1.getId(), b1);
        beerMap.put(b2.getId(), b2);
        beerMap.put(b3.getId(), b3);

    }

    @Override
    public List<Beer> getBeerList(){
        return new ArrayList<>(beerMap.values());
    }
    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Getting beer by ID in Service: "  + id.toString());
        return beerMap.get(id);
    }
}
