package codenomads.carhire_search.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CarhireSearchCriteria {
    private Date startDate;
    private Date endDate;
    private Double maxPrice;
    private Double minPrice;
    private String type;
    private Integer numPassengers;
}