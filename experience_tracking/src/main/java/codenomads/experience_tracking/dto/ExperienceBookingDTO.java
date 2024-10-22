package codenomads.experience_tracking.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ExperienceBookingDTO {
    private BookingDTO booking;
    private ExperienceDTO experience;
    private BigDecimal amountPaid; // Optional field for amount paid
    private Long tripId;           // Optional field for trip ID
    private Set<Long> responsibleUserIds;

    @Data
    public static class BookingDTO {
        private String id;
        private String experienceId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Date startDate;
        private int participants;
    }

    @Data
    public static class ExperienceDTO {
        private String id;
        private String organizer;
        private String eventName;
        private String address;
        private String city;
        private String state;
        private int postcode;
        private String borough;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String nta;
        private int capacity;
        private BigDecimal pricePerPerson;
    }
}