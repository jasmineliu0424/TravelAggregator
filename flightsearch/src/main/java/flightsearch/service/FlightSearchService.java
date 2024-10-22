package flightsearch.service;

import flightsearch.dto.FlightSearchResponse;
import flightsearch.gateway.FlightsApiGateway;
import flightsearch.repository.FlightSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class FlightSearchService {

    private final FlightSearchRepository flightSearchRepository;
    private final FlightsApiGateway flightsApiGateway;

    @Autowired
    public FlightSearchService(FlightSearchRepository flightSearchRepository, FlightsApiGateway flightsApiGateway) {
        this.flightSearchRepository = flightSearchRepository;
        this.flightsApiGateway = flightsApiGateway;
    }

    public FlightSearchResponse getFlights(Map<String,String> params) {
        try {
            FlightSearchResponse response = this.flightsApiGateway.getFromFlightsApi("", params).block();
            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return new FlightSearchResponse(new ArrayList<>());
    }
}
