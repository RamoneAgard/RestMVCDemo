package agard.spring.restmvc.services;

import agard.spring.restmvc.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getCustomerList();

    Customer getCustomerById(int id);
}
