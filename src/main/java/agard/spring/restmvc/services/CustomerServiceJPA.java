package agard.spring.restmvc.services;

import agard.spring.restmvc.domain.Customer;
import agard.spring.restmvc.mappers.CustomerMapper;
import agard.spring.restmvc.model.CustomerDTO;
import agard.spring.restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Primary
@Service
public class CustomerServiceJPA implements CustomerService {

    // Fields//
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    // Methods //
    @Override
    public List<CustomerDTO> getCustomerList(String customerName) {
        List<Customer> customerList;
        if(StringUtils.hasText(customerName)){
            customerList = listCustomersByName(customerName);
        } else {
            customerList = customerRepository.findAll();
        }
        return customerList
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    private List<Customer> listCustomersByName(String customerName){
        return customerRepository.findAllByCustomerNameIsLikeIgnoreCase("%" + customerName + "%");
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(
                customerMapper.customerToCustomerDto(
                        customerRepository.findById(id)
                                .orElse(null)));

    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        return customerMapper.customerToCustomerDto(
                customerRepository.save(
                        customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
        customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {
            foundCustomer.setCustomerName(customer.getCustomerName());
            if(StringUtils.hasText(customer.getEmail())){
                foundCustomer.setEmail(customer.getEmail());
            }

            atomicReference.set(Optional.of(
                    customerMapper.customerToCustomerDto(
                            customerRepository.save(foundCustomer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID customerId) {
        if(customerRepository.existsById(customerId)){
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(foundCustomer ->  {
            // check Name
            if(StringUtils.hasText(customer.getCustomerName())){
                foundCustomer.setCustomerName(customer.getCustomerName());
            }
            if(StringUtils.hasText(customer.getEmail())){
                foundCustomer.setEmail(customer.getEmail());
            }

            atomicReference.set(Optional.of(
                    customerMapper.customerToCustomerDto(
                            customerRepository.save(foundCustomer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
