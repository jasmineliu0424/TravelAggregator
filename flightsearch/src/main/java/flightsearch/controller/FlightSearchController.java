package flightsearch.controller;

import flightsearch.dto.FlightSearchResponse;
import flightsearch.model.Flight;
import flightsearch.service.FlightSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/flightsearch")
public class FlightSearchController {
    private final FlightSearchService flightSearchService;

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @GetMapping("/flights")
    public ResponseEntity<FlightSearchResponse> getFlights(@RequestParam Map<String,String> allParams) {
        try {
            return ResponseEntity.ok(this.flightSearchService.getFlights(allParams));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
