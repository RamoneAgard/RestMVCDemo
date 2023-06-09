package agard.spring.restmvc.bootstrap;

import agard.spring.restmvc.domain.Beer;
import agard.spring.restmvc.domain.Customer;
import agard.spring.restmvc.model.BeerCSVRecord;
import agard.spring.restmvc.model.BeerStyle;
import agard.spring.restmvc.repositories.BeerRepository;
import agard.spring.restmvc.repositories.CustomerRepository;
import agard.spring.restmvc.services.BeerCsvService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;

    private final CustomerRepository customerRepository;

    private final BeerCsvService beerCsvService;


    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCsvData();
        loadCustomerData();
    }

    private void loadCsvData() throws FileNotFoundException {
        if(beerRepository.count() < 10){
            File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
            List<BeerCSVRecord> records = beerCsvService.convertCSV(file);

            records.forEach(beerCSVRecord -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager"
                        -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale"
                        -> BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA"
                            -> BeerStyle.IPA;
                    case "American Porter"
                            -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout"
                            -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale"
                            -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier"
                            -> BeerStyle.WHEAT;
                    case "English Pale Ale"
                            -> BeerStyle.PALE_ALE;
                    default
                            -> BeerStyle.PILSNER;
                };

                beerRepository.save(Beer.builder()
                        .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                        .beerStyle(beerStyle)
                        .price(BigDecimal.TEN)
                        .upc(beerCSVRecord.getRow().toString())
                        .quantityOnHand(beerCSVRecord.getCount())
                        .build());

            });
        }
    }


    private void loadBeerData(){
        if(beerRepository.count() == 0){
            Beer beer1 = Beer.builder()
                    .beerName("City Wide")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("98765")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(15)
//                    .createdDate(LocalDateTime.now())
//                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer2 = Beer.builder()
                    .beerName("Elysian Space Dust")
                    .beerStyle(BeerStyle.IPA)
                    .upc("12345")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(12)
//                    .createdDate(LocalDateTime.now())
//                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer3 = Beer.builder()
                    .beerName("Red Stripe")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("45678")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(7)
//                    .createdDate(LocalDateTime.now())
//                    .updateDate(LocalDateTime.now())
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
//                    .createdDate(LocalDateTime.now())
//                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer c2 = Customer.builder()
                    .customerName("Customer Two")
//                    .createdDate(LocalDateTime.now())
//                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer c3 = Customer.builder()
                    .customerName("Customer Three")
//                    .createdDate(LocalDateTime.now())
//                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.saveAll(Arrays.asList(c1, c2, c3));
        }


    }
}
