package agard.spring.restmvc.controllers;


import agard.spring.restmvc.model.Beer;
import agard.spring.restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_ID_PARAM = "beerId";
    public static final String BEER_PATH_ID = BEER_PATH +  "/{"+BEER_ID_PARAM+"}";


    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    public List<Beer> beersList(){
        return beerService.getBeerList();
    }

    @GetMapping(BEER_PATH_ID)
    public Beer getBeerById(@PathVariable(BEER_ID_PARAM) UUID beerId){
        log.debug("Getting beer by ID in Controller -12222");
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity handlePost(@RequestBody Beer beer){
        Beer savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH  + savedBeer.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateById(@PathVariable(BEER_ID_PARAM) UUID beerId, @RequestBody Beer beer){

        beerService.updateBeerById(beerId, beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable(BEER_ID_PARAM) UUID beerId){
        beerService.deleteById(beerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchById(@PathVariable(BEER_ID_PARAM) UUID beerId, @RequestBody Beer beer){

        beerService.patchBeerById(beerId, beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
