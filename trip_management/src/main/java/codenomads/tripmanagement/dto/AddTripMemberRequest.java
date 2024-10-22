package codenomads.tripmanagement.dto;

import codenomads.tripmanagement.domain.TripMember;
import lombok.Data;

@Data
public class AddTripMemberRequest {
    private Long userId;
    private TripMember.Role role;
}
