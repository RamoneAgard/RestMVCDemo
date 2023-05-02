package agard.spring.restmvc.services;

import agard.spring.restmvc.mappers.BeerMapper;
import agard.spring.restmvc.model.BeerDTO;
import agard.spring.restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Primary
@Service
public class BeerServiceJPA implements BeerService {

    // Fields //
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    // Methods //
    @Override
    public List<BeerDTO> getBeerList() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
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
    public void updateBeerById(UUID beerId, BeerDTO beer) {

    }

    @Override
    public void deleteById(UUID beerID) {

    }

    @Override
    public void patchBeerById(UUID beerId, BeerDTO beer) {

    }
}
