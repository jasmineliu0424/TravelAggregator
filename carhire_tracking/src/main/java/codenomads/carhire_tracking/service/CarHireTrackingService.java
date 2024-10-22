package codenomads.carhire_tracking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import codenomads.carhire_tracking.domain.CarHireBooking;
import codenomads.carhire_tracking.dto.CarHireBookingDTO;
import codenomads.carhire_tracking.repository.CarHireBookingRepository;
import jakarta.transaction.Transactional;
import codenomads.carhire_tracking.gateway.ExpenseGateway;
import codenomads.carhire_tracking.gateway.TripGateway;

import java.util.UUID;

@Service
public class CarHireTrackingService {

    private final CarHireBookingRepository carHireBookingRepository;
    private final ExpenseGateway expenseGateway;
    private final TripGateway tripGateway;

    @Autowired
    public CarHireTrackingService(CarHireBookingRepository carHireBookingRepository, ExpenseGateway expenseGateway, TripGateway tripGateway) {
        this.carHireBookingRepository = carHireBookingRepository;
        this.expenseGateway = expenseGateway;
        this.tripGateway = tripGateway;
    }

    @Transactional
    public CarHireBooking saveCarHireBooking(CarHireBookingDTO dto) {
        CarHireBooking booking = new CarHireBooking();
        // Set car hire details
        booking.setBookingId(dto.getData().getBooking().getId());
        booking.setCarHireId(dto.getData().getBooking().getCarHireId());
        booking.setOperator(dto.getData().getCarhire().getOperator());
        booking.setLicensePlate(dto.getData().getCarhire().getLicensePlate());
        booking.setVin(dto.getData().getCarhire().getVin());
        booking.setVehicleYear(dto.getData().getCarhire().getVehicleYear());
        booking.setPhone(dto.getData().getCarhire().getPhone());
        booking.setWebsite(dto.getData().getCarhire().getWebsite());
        booking.setDailyRate(dto.getData().getCarhire().getDailyRate());
        booking.setVehicleType(dto.getData().getCarhire().getVehicleType());
        booking.setSeatCapacity(dto.getData().getCarhire().getSeatCapacity());
        
        // Set booking details
        booking.setStartDate(dto.getData().getBooking().getStartDate());
        booking.setEndDate(dto.getData().getBooking().getEndDate());

        CarHireBooking savedBooking = carHireBookingRepository.save(booking);

        if (dto.getAmountPaid() != null) {
            if (dto.getTripId() == null || dto.getResponsibleUserIds() == null || dto.getResponsibleUserIds().isEmpty()) {
                throw new IllegalArgumentException("TripId and responsibleUserIds are required when amountPaid is provided");
            }
            Long expenseId = expenseGateway.createExpense(dto.getAmountPaid(), dto.getTripId(), dto.getResponsibleUserIds());
            savedBooking.setExternalExpenseReference(expenseId);
            carHireBookingRepository.save(savedBooking);
        }

        if (dto.getTripId() != null) {
            tripGateway.addToTrip(dto.getTripId(), savedBooking.getId());
        }

        return savedBooking;
    }
}