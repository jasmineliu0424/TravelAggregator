package codenomads.experience_search.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ExperienceSearchCriteria {
    private Date date;
    private Double maxPrice;
    private Double minPrice;
    private String organizer;
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer numberOfParticipants;
}