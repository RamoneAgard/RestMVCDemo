package agard.spring.restmvc.services;

import agard.spring.restmvc.model.Beer;
import agard.spring.restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<Integer, Customer> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();
        Customer c1 = Customer.builder()
                .id(1)
                .customerName("Customer One")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer c2 = Customer.builder()
                .id(2)
                .customerName("Customer Two")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer c3 = Customer.builder()
                .id(3)
                .customerName("Customer Three")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(c1.getId(), c1);
        customerMap.put(c2.getId(), c2);
        customerMap.put(c3.getId(), c3);
    }

    @Override
    public List<Customer> getCustomerList() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getCustomerById(int id) {
        log.debug("Getting Customer by Id in Service: " + id);
        return customerMap.get(id);
    }
}
