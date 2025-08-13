package flightsearch.controller;

import flightsearch.dto.FlightSearchCriteria;
import flightsearch.dto.FlightSearchResponse;
import flightsearch.service.FlightSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flightsearch")
public class FlightSearchController {
    private final FlightSearchService flightSearchService;

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @PostMapping("/flights")
    public ResponseEntity<FlightSearchResponse> searchFlights(@RequestBody FlightSearchCriteria criteria) {
        try {
            return ResponseEntity.ok(this.flightSearchService.searchFlights(criteria));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}