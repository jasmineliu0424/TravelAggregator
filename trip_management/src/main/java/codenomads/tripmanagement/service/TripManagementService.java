package codenomads.tripmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codenomads.tripmanagement.domain.Trip;
import codenomads.tripmanagement.domain.TripMember;
import codenomads.tripmanagement.dto.CreateTripRequest;
import codenomads.tripmanagement.domain.BookingSource;
import codenomads.tripmanagement.repository.TripRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TripManagementService {

    private final TripRepository tripRepository;

    @Autowired
    public TripManagementService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Transactional
    public Trip createTrip(CreateTripRequest request, Long creatorId) {
        Trip trip = new Trip();
        trip.setTripName(request.getTripName());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate()); 
        trip.setCreatorId(creatorId);
        TripMember creator = new TripMember();
        creator.setUserId(creatorId);
        creator.setRole(TripMember.Role.CREATOR);
        trip.addMember(creator);
        if (request.getMembers() == null){
            return tripRepository.save(trip);
        }
        for (Long memberId : request.getMembers()) {
            if (memberId.equals(creatorId)) {
                continue;
            }
            TripMember member = new TripMember();
            member.setUserId(memberId);
            member.setRole(TripMember.Role.MEMBER);
            trip.addMember(member);
        }
        return tripRepository.save(trip);
    }

    @Transactional
    public Optional<Trip> addBookingToTrip(Long tripId, Long bookingId, BookingSource source) {
        // Idempotency: dedupe on (bookingId, source)
        boolean exists = trip.getBookings().stream().anyMatch(b ->
            Objects.equals(bookingId, b.getBookingId()) &&
            b.getSource() == source // enum compare: use ==
        );

        if (!exists) {
            Trip.Booking booking = new Trip.Booking();
            booking.setBookingId(bookingId);
            booking.setSource(source);
            trip.getBookings().add(booking);
            tripRepository.save(trip);
        }

        return trip;
    }

    @Transactional
    public Optional<Trip> addMemberToTrip(Long tripId, Long userId, TripMember.Role role) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        optionalTrip.ifPresent(trip -> {
            TripMember member = new TripMember();
            member.setUserId(userId);
            member.setRole(role);
            trip.addMember(member);
            tripRepository.save(trip);
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
