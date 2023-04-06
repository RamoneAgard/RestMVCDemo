package agard.spring.restmvc.controllers;


import agard.spring.restmvc.model.Beer;
import agard.spring.restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {
    private final BeerService beerService;

    public List<Beer> beersList(){
        return beerService.getBeerList();
    }

    public Beer getBeerById(UUID id){
        log.debug("Getting beer by ID in Controller");
        return beerService.getBeerById(id);
    }
}
