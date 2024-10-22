package codenomads.tripmanagement.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "trips")
@Data
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tripName;
    private Date startDate;
    private Date endDate;
    private Long creatorId;

    @ElementCollection
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TripMember> members = new HashSet<>();

    @Embeddable
    @Data
    public static class Booking {
        private Long bookingId;
        @Enumerated(EnumType.STRING)
        private BookingSource source;
    }

    public void addMember(TripMember member) {
        member.setTrip(this);
        members.add(member);
    }

    public void removeMember(Long userId) {
        members.removeIf(member -> member.getUserId().equals(userId));
    }
}