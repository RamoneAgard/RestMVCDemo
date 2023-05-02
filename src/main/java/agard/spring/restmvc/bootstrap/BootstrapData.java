package agard.spring.restmvc.bootstrap;

import agard.spring.restmvc.domain.Beer;
import agard.spring.restmvc.domain.Customer;
import agard.spring.restmvc.model.BeerStyle;
import agard.spring.restmvc.model.CustomerDTO;
import agard.spring.restmvc.repositories.BeerRepository;
import agard.spring.restmvc.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadBeerData(){
        if(beerRepository.count() == 0){
            Beer beer1 = Beer.builder()
                    .beerName("City Wide")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("98765")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(15)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer2 = Beer.builder()
                    .beerName("Elysian Space Dust")
                    .beerStyle(BeerStyle.IPA)
                    .upc("12345")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(12)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer3 = Beer.builder()
                    .beerName("Red Stripe")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("45678")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(7)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            beerRepository.save(beer1);
            beerRepository.save(beer2);
            beerRepository.save(beer3);
        }



    }

    private void loadCustomerData(){
        if(customerRepository.count() == 0){
            Customer c1 = Customer.builder()
                    .customerName("Customer One")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer c2 = Customer.builder()
                    .customerName("Customer Two")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer c3 = Customer.builder()
                    .customerName("Customer Three")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.saveAll(Arrays.asList(c1, c2, c3));
        }


    }
}
