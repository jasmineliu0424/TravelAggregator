package codenomads.tripmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codenomads.tripmanagement.domain.Trip;
import codenomads.tripmanagement.domain.TripMember;
import codenomads.tripmanagement.dto.CreateTripRequest;
import codenomads.tripmanagement.domain.BookingSource;
import codenomads.tripmanagement.repository.TripRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TripManagementService {

    private final TripRepository tripRepository;

    @Autowired
    public TripManagementService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Transactional
    public Trip createTrip(CreateTripRequest request, Long creatorId) {
        // --- date guard (reject inconsistent dates) ---
        if (request.getStartDate() != null && request.getEndDate() != null
                && request.getStartDate().after(request.getEndDate())) {
            throw new IllegalArgumentException("startDate must not be after endDate");
        }
        
        Trip trip = new Trip();
        trip.setTripName(request.getTripName());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate()); 
        trip.setCreatorId(creatorId);

         // --- normalize members to set semantics; ensure creator exactly once ---
        Set<Long> normalized = new LinkedHashSet<>();
        normalized.add(creatorId); // creator always present

        if (request.getMembers() != null) {
            for (Long id : request.getMembers()) {
                if (id != null) {
                    normalized.add(id);
                }
            }
        }

        for (Long id : normalized) {
            TripMember m = new TripMember();
            m.setUserId(id);
            m.setRole(id.equals(creatorId) ? TripMember.Role.CREATOR : TripMember.Role.MEMBER);
            trip.addMember(m);
        }
        
        return tripRepository.save(trip);
    }

    @Transactional
    public Optional<Trip> addBookingToTrip(Long tripId, Long bookingId, BookingSource source) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        optionalTrip.ifPresent(trip -> {
            Trip.Booking booking = new Trip.Booking();
            booking.setBookingId(bookingId);
            booking.setSource(source);
            trip.getBookings().add(booking);
            tripRepository.save(trip);
        });
        return optionalTrip;
    }
    //if the member already exists, no duplicate is added.
    @Transactional
    public Optional<Trip> addMemberToTrip(Long tripId, Long userId, TripMember.Role role) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        optionalTrip.ifPresent(trip -> {
            boolean exists = trip.getMembers().stream()
                .anyMatch(m -> m.getUserId().equals(userId));
            if (!exists) {
                TripMember member = new TripMember();
                member.setUserId(userId);
                member.setRole(role);
                trip.addMember(member);
                tripRepository.save(trip);
            }
        });
        return optionalTrip;
    }

    @Transactional
    public Optional<Trip> removeMemberFromTrip(Long tripId, Long userId) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        optionalTrip.ifPresent(trip -> {
            trip.removeMember(userId);
            tripRepository.save(trip);
        });
        return optionalTrip;
    }

    @Transactional
    public Trip updateTrip(Long tripId, Trip updatedTrip) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        if (optionalTrip.isPresent()) {
            // --- date guard (reject inconsistent dates) ---
            if (updatedTrip.getStartDate() != null && updatedTrip.getEndDate() != null
                    && updatedTrip.getStartDate().after(updatedTrip.getEndDate())) {
                throw new IllegalArgumentException("startDate must not be after endDate");
            }

            Trip trip = optionalTrip.get();
            trip.setTripName(updatedTrip.getTripName());
            trip.setStartDate(updatedTrip.getStartDate());
            trip.setEndDate(updatedTrip.getEndDate());
            return tripRepository.save(trip);
        }
        return null;
    }

    @Transactional
    public void removeTrip(Long tripId) {
        tripRepository.deleteById(tripId);
    }

    public List<Trip> queryTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> queryTripById(Long tripId) {
        return tripRepository.findById(tripId);
    }

    public boolean isUserInTrip(Long tripId, Long userId) {
        return tripRepository.existsByIdAndMembersUserId(tripId, userId);
    }

    public boolean isUserCreator(Long tripId, Long userId) {
        Optional<Trip> tripOpt = tripRepository.findById(tripId);
        return tripOpt.map(trip -> trip.getCreatorId().equals(userId)).orElse(false);
    }
}
