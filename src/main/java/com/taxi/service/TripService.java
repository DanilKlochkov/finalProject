package com.taxi.service;

import com.taxi.dto.TripDto;
import com.taxi.dto.TripResponse;
import com.taxi.entity.Trip;
import com.taxi.enums.OrderStatusEnum;
import com.taxi.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final OrderService orderService;
    private final DriverService driverService;
    private final PassengerService passengerService;

    private TripResponse toResponse(Trip trip) {
        return TripResponse.builder()
                .id(trip.getId())
                .passengerName(trip.getOrder().getPassenger().getUserName())
                .driverName(trip.getDriver().getUserName())
                .cost(trip.getOrder().getCost())
                .spendDriverTime(trip.getArrivalTime().until(trip.getStartTime(), ChronoUnit.MINUTES))
                .spendPassengerTime(trip.getOrder().getOrderTime().until(trip.getArrivalTime(), ChronoUnit.MINUTES))
                .tripTime(trip.getStartTime().until(trip.getEndTime(), ChronoUnit.MINUTES))
                .stars(trip.getStars())
                .comment(trip.getComment())
                .build();
    }

    public void createTrip(int orderId, String driverId) throws AccessDeniedException {
        var driver = driverService.findDriverById(driverId);
        if (driver.getRating() < 4.8) {
            throw new AccessDeniedException("You're missing a rating");
        }
        var order = orderService.findOrderById(orderId);
        order.setStatus(String.valueOf(OrderStatusEnum.ACCEPTED));
        var trip = new Trip();
        trip.setOrder(order);
        trip.setDriver(driver);
        tripRepository.save(trip);
    }

    public void update(TripDto trip, int id) throws NoSuchElementException {
        var updateTrip = tripRepository.findById(id).orElseThrow(NoSuchElementException::new);

        if (trip.getStars() <=5 && trip.getStars() >= 1) {
            var driver = driverService.findDriverById(updateTrip.getDriver().getUserName());
            driver.setStarCount(driver.getStarCount() + 1);
            driver.setRating(
                    ((driver.getStarCount() - 1) * driver.getRating() + trip.getStars()) / driver.getStarCount()
            );
            updateTrip.setStars(trip.getStars());
        }
        if (Objects.nonNull(trip.getComment())) {
            updateTrip.setComment(trip.getComment());
        }
        if (Objects.nonNull(trip.getStartTime())) {
            updateTrip.setStartTime(trip.getStartTime());
        }
        if (Objects.nonNull(trip.getArrivalTime())) {
            updateTrip.setArrivalTime(trip.getArrivalTime());
        }
        if (Objects.nonNull(trip.getEndTime())) {
            updateTrip.setEndTime(trip.getEndTime());
        }

        tripRepository.save(updateTrip);
    }

    public List<TripResponse> getDriverStatistic(OffsetDateTime from, OffsetDateTime to, String driverId) {
        var driver = driverService.findDriverById(driverId);
        return tripRepository.findByTimeAndDriverId(from, to, driver)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TripResponse> getPassengerStatistic(OffsetDateTime from, OffsetDateTime to, String passengerId) {
        var passenger = passengerService.findById(passengerId);
        return tripRepository.findByTimeAndPassengerId(from, to, passenger)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
