package agard.spring.restmvc.services;

import agard.spring.restmvc.model.BeerDTO;
import agard.spring.restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        beerMap = new HashMap<>();

        BeerDTO b1 = BeerDTO.builder()
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

        BeerDTO b2 = BeerDTO.builder()
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

        BeerDTO b3 = BeerDTO.builder()
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
    public List<BeerDTO> getBeerList(){
        return new ArrayList<>(beerMap.values());
    }
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("Getting beer by ID in Service: "  + id.toString());
        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {

        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(beer.getVersion())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .price(beer.getPrice())
                .upc(beer.getUpc())
                .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        BeerDTO existing = beerMap.get(beerId);
        existing.setBeerName(beer.getBeerName());
        existing.setBeerStyle(beer.getBeerStyle());
        existing.setPrice(beer.getPrice());
        existing.setUpc(beer.getUpc());
        existing.setQuantityOnHand(beer.getQuantityOnHand());
        existing.setUpdateDate(LocalDateTime.now());

        beerMap.put(existing.getId(), existing);

        return Optional.of(existing);
    }

    @Override
    public Boolean deleteById(UUID beerID) {
        beerMap.remove(beerID);
        return true;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer){
        BeerDTO existing = beerMap.get(beerId);

        if(StringUtils.hasText(beer.getBeerName())){
            existing.setBeerName(beer.getBeerName());
        }
        if(beer.getBeerStyle() != null){
            existing.setBeerStyle(beer.getBeerStyle());
        }
        if(beer.getPrice() != null){
            existing.setPrice(beer.getPrice());
        }
        if(beer.getQuantityOnHand() != null){
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }
        if(StringUtils.hasText(beer.getUpc())){
            existing.setUpc(beer.getUpc());
        }

        beerMap.put(existing.getId(), existing);

        return Optional.of(existing);
    }
}
