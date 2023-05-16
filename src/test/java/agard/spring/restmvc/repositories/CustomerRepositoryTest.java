package agard.spring.restmvc.repositories;

import agard.spring.restmvc.bootstrap.BootstrapData;
import agard.spring.restmvc.domain.Beer;
import agard.spring.restmvc.domain.Customer;
import agard.spring.restmvc.model.BeerStyle;
import agard.spring.restmvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        Customer savedCustomer = customerRepository.save(Customer.builder()
                .customerName("My Test Customer")
                .build());

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
    }

    @Test
    void testSaveNameTooLong() {
        assertThrows(ConstraintViolationException.class, () ->   {
            Customer savedCustomer = customerRepository.save(Customer.builder()
                    .customerName("My Test Beer My Test Beer My Test Beer My Test Beer My Test Beer My Test Beer")
                    .email("new.guy@gmail.com")
                    .build());

            customerRepository.flush();
        });
    }

    @Test
    void testSaveInvalidEmail() {
        assertThrows(ConstraintViolationException.class, () ->   {
            Customer savedCustomer = customerRepository.save(Customer.builder()
                    .customerName("My Test Customer")
                    .email("new.guy(at)WTF.com.com")
                    .build());

            customerRepository.flush();
        });
    }

    @Test
    void testGetCustomerListByName() {
        List<Customer> list = customerRepository.findAllByCustomerNameIsLikeIgnoreCase("%two%");

        assertThat(list.size()).isEqualTo(1);
    }

}