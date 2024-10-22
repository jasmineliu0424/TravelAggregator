package codenomads.hotel_tracking.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HotelBookingDTO {
    private HotelData data;
    private BigDecimal amountPaid;
    private Long tripId;
    private Set<Long> responsibleUserIds;

    @Data
    public static class HotelData {
        private HotelDTO hotel;
        private BookingDTO booking;
    }

    @Data
    public static class HotelDTO {
        @JsonProperty("hotel_id")
        private String hotelId;
        private Integer borocode;
        private Integer block;
        private Integer lot;
        @JsonProperty("street_number")
        private String streetNumber;
        @JsonProperty("street_name")
        private String streetName;
        private Integer zipcode;
        @JsonProperty("building_class")
        private String buildingClass;
        @JsonProperty("owner_name")
        private String ownerName;
        private String borough;
        private String latitude;
        private String longitude;
        private String area;
    }

    @Data
    public static class BookingDTO {
        @JsonProperty("hotel_id")
        private String hotelId;
        private String id;
        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Date startDate;
        @JsonProperty("end_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Date endDate;
        @JsonProperty("one_person_rooms")
        private Integer onePersonRooms;
        @JsonProperty("two_person_rooms")
        private Integer twoPersonRooms;
        @JsonProperty("four_person_rooms")
        private Integer fourPersonRooms;
    }
}