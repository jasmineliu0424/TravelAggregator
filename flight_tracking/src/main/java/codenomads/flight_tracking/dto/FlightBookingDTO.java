package codenomads.flight_tracking.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import java.util.Set;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FlightBookingDTO {
    private FlightData data;
    private BigDecimal amountPaid;
    private Long tripId;
    private Set<Long> responsibleUserIds;

    @Data
    public static class FlightData {
        @JsonProperty("bookingid")
        @Nullable
        private UUID bookingId;
        @JsonProperty("flightid")
        private UUID flightId;
        private String carrier;
        @JsonProperty("flightnumber")
        private String flightNumber;
        private String origin;
        private String destination;
        @JsonProperty("distancemiles")
        private Integer distanceMiles;
        @JsonProperty("totalseats")
        private Integer totalSeats;
        @JsonProperty("availableseats")
        private Integer availableSeats;
        private String aircraft;
        private Integer airtime;
        @JsonProperty("departure_datetime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Date departureDateTime;
        @JsonProperty("flight_price")
        private BigDecimal flightPrice;
    }
}