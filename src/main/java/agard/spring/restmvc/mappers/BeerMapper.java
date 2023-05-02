package agard.spring.restmvc.mappers;

import agard.spring.restmvc.domain.Beer;
import agard.spring.restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);
}
