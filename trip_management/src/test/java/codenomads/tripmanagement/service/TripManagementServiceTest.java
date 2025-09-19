package codenomads.tripmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import codenomads.tripmanagement.domain.BookingSource;
import codenomads.tripmanagement.domain.Trip;
import codenomads.tripmanagement.domain.TripMember;
import codenomads.tripmanagement.dto.CreateTripRequest;
import codenomads.tripmanagement.repository.TripRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripManagementServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripManagementService tripManagementService;

    private CreateTripRequest testRequest;
    private Trip testTrip;

    @BeforeEach
    void setUp() {
        testRequest = createTestRequest();
        testTrip = createTestTrip();
    }

    @Test
    void createTripValidRequestWithMembersShouldCreateTripWithAllMembers() {
        testRequest.setMembers(Arrays.asList(1L, 2L, 3L));
        when(tripRepository.save(any(Trip.class))).thenReturn(testTrip);

        Trip result = tripManagementService.createTrip(testRequest, 1L);

        assertNotNull(result);
        verify(tripRepository, times(1)).save(argThat(trip -> 
            trip.getTripName().equals(testRequest.getTripName()) &&
            trip.getStartDate().equals(testRequest.getStartDate()) &&
            trip.getEndDate().equals(testRequest.getEndDate()) &&
            trip.getCreatorId().equals(1L) &&
            trip.getMembers().size() == 3 // Creator + 2 additional members
        ));
    }

    @Test
    void createTripValidRequestWithoutMembersShouldCreateTripWithCreatorOnly() {
        testRequest.setMembers(null);
        when(tripRepository.save(any(Trip.class))).thenReturn(testTrip);

        Trip result = tripManagementService.createTrip(testRequest, 1L);

        assertNotNull(result);
        verify(tripRepository, times(1)).save(argThat(trip -> 
            trip.getCreatorId().equals(1L) &&
            trip.getMembers().size() == 1 // Only creator
        ));
    }

    @Test
    void createTripCreatorInMembersListShouldNotDuplicateCreator() {
        testRequest.setMembers(Arrays.asList(1L, 2L)); // Creator (1L) is in members list
        when(tripRepository.save(any(Trip.class))).thenReturn(testTrip);

        Trip result = tripManagementService.createTrip(testRequest, 1L);

        assertNotNull(result);
        verify(tripRepository, times(1)).save(argThat(trip -> 
            trip.getMembers().size() == 2 // Creator + 1 additional member (no duplication)
        ));
    }


    @Test
    void isUserInTripUserInTripShouldReturnTrue() {
        when(tripRepository.existsByIdAndMembersUserId(1L, 1L)).thenReturn(true);

        boolean result = tripManagementService.isUserInTrip(1L, 1L);

        assertTrue(result);
        verify(tripRepository, times(1)).existsByIdAndMembersUserId(1L, 1L);
    }

    private CreateTripRequest createTestRequest() {
        CreateTripRequest request = new CreateTripRequest();
        request.setTripName("Test Trip");
        request.setStartDate(new Date());
        request.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        return request;
    }

    private Trip createTestTrip() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setTripName("Test Trip");
        trip.setStartDate(new Date());
        trip.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        trip.setCreatorId(1L);
        return trip;
    }
}