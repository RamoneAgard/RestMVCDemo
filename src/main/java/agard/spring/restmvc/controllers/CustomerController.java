package agard.spring.restmvc.controllers;

import agard.spring.restmvc.model.CustomerDTO;
import agard.spring.restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customer/";
    public static final String CUSTOMER_ID_PARAM = "customerID";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "{"+ CUSTOMER_ID_PARAM +"}";


    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> customersList(@RequestParam(required = false) String customerName){
        return customerService.getCustomerList(customerName);
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable(CUSTOMER_ID_PARAM) UUID customerId){
        log.debug("Getting customer By Id in Controller");
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity handlePost(@Validated @RequestBody CustomerDTO customer){
        CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMER_PATH + savedCustomer.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateById(@PathVariable(CUSTOMER_ID_PARAM) UUID customerId, @Validated @RequestBody CustomerDTO customer){
        if(customerService.updateCustomerById(customerId, customer).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable(CUSTOMER_ID_PARAM)UUID customerId){
        if(!customerService.deleteById(customerId)){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchById(@PathVariable(CUSTOMER_ID_PARAM) UUID customerId, @RequestBody CustomerDTO customer){
        if(customerService.patchCustomerById(customerId, customer).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
