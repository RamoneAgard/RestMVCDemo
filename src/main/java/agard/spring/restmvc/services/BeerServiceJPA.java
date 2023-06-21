package agard.spring.restmvc.services;

import agard.spring.restmvc.domain.Beer;
import agard.spring.restmvc.mappers.BeerMapper;
import agard.spring.restmvc.model.BeerDTO;
import agard.spring.restmvc.model.BeerStyle;
import agard.spring.restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Primary
@Service
public class BeerServiceJPA implements BeerService {

    // Fields //
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;

    // Methods //
    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize){
        int queryPageNumber;
        int queryPageSize;

        if(pageNumber != null && pageNumber > 0){
            queryPageNumber = pageNumber - 1;
        }
        else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if(pageSize != null && (pageSize > 0 && pageSize <= 1000)){
            queryPageSize = pageSize;
        }
        else {
            queryPageSize = DEFAULT_PAGE_SIZE;
        }

        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageable);
    }

    private Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }

    private Page<Beer> listBeersByName(String beerName, Pageable pageable){
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
    }

    @Override
    public Page<BeerDTO> getBeerList(String beerName, BeerStyle beerStyle, Boolean showInventory,
                                     Integer pageNumber, Integer pageSize) {

        Page<Beer>  beerPage;
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        //get data from parameters
        if(StringUtils.hasText(beerName) && beerStyle == null){
            beerPage = listBeersByName(beerName, pageRequest);
        }
        else if(!StringUtils.hasText(beerName) && beerStyle != null){
            beerPage = listBeersByStyle(beerStyle, pageRequest);
        }
        else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
        } else{
            beerPage = beerRepository.findAll(pageRequest);
        }

        // show inventory
        if(showInventory != null && !showInventory){
            beerPage.forEach(beer -> {
                beer.setQuantityOnHand(null);
            });
        }

        return beerPage.map(beerMapper::beerToBeerDto);

//        return beerPage
//                .stream()
//                .map(beerMapper::beerToBeerDto)
//                .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(
                beerMapper.beerToBeerDto(
                    beerRepository.findById(id)
                            .orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return beerMapper.beerToBeerDto(
                beerRepository.save(
                        beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            beerRepository.save(foundBeer);
            atomicReference.set(Optional.of(
                    beerMapper.beerToBeerDto(
                            beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID beerID) {
        if(beerRepository.existsById(beerID)){
            beerRepository.deleteById(beerID);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference  =  new AtomicReference<>();
        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            // name
            if(StringUtils.hasText(beer.getBeerName())){
                foundBeer.setBeerName(beer.getBeerName());
            }
            //style
            if(beer.getBeerStyle() != null){
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }
            //price
            if(beer.getPrice() != null){
                foundBeer.setPrice(beer.getPrice());
            }
            //quantity
            if(beer.getQuantityOnHand() != null){
                foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }
            //upc
            if(StringUtils.hasText(beer.getUpc())){
                foundBeer.setUpc(beer.getUpc());
            }

            atomicReference.set(Optional.of(
                    beerMapper.beerToBeerDto(
                            beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
