package agard.spring.restmvc.controllers;

import agard.spring.restmvc.model.Customer;
import agard.spring.restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> customersList(){
        return customerService.getCustomerList();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") int customerId){
        log.debug("Getting customer By Id in Controller");
        return customerService.getCustomerById(customerId);
    }

}
