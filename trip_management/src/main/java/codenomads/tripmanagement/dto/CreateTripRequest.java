package codenomads.tripmanagement.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class CreateTripRequest {
    private String tripName;
    private Date startDate;
    private Date endDate;
    private List<Long> members;
}
