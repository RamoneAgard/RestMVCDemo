package agard.spring.restmvc.repositories;

import agard.spring.restmvc.domain.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {

}
