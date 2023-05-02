package agard.spring.restmvc.mappers;

import agard.spring.restmvc.domain.Customer;
import agard.spring.restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);
}
