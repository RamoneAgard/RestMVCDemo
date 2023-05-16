package agard.spring.restmvc.repositories;

import agard.spring.restmvc.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    List<Customer> findAllByCustomerNameIsLikeIgnoreCase(String customerName);
}

