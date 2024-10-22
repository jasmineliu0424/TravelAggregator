package hotelsearch.controller;

import hotelsearch.dto.HotelsApiSearchRequest;
import hotelsearch.dto.HotelsApiSearchResponse;
import hotelsearch.service.HotelSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hotelsearch")
public class HotelSearchController {
    private final HotelSearchService hotelSearchService;

    public HotelSearchController(HotelSearchService hotelSearchService) {
        this.hotelSearchService = hotelSearchService;
    }

    @PostMapping("/hotels")
    public ResponseEntity<HotelsApiSearchResponse> getHotels(@RequestBody HotelsApiSearchRequest hotelSearchRequest) {
        return ResponseEntity.ok(this.hotelSearchService.getHotels(hotelSearchRequest));
    }
}
