package com.taxi.controller;

import com.taxi.dto.TripDto;
import com.taxi.dto.TripResponse;
import com.taxi.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TripController {
    private final TripService tripService;

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral(' ')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .toFormatter(Locale.ROOT);

    @PostMapping("/newTrip")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<String> createNewTrip(
            @RequestParam int orderId,
            @RequestParam String driverId
    ) {
        try {
            tripService.createTrip(orderId, driverId);
            log.info("Create new trip with order = " + orderId + " and driver = " + driverId);
            return ResponseEntity
                    .ok()
                    .body("Success create new trip!");
        } catch (AccessDeniedException e) {
            log.warn("Error with creating new trip with order = " + orderId + " and driver = " + driverId);
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/comment")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<String> commentTrip(
            @RequestParam String comment,
            @RequestParam int tripId
    ) {
        try {
            var tripDto = new TripDto();
            tripDto.setComment(comment);
            tripService.update(tripDto, tripId);
            return ResponseEntity
                    .ok("Thanks for your comment!");
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @PutMapping("/star")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<String> star(
            @RequestParam int star,
            @RequestParam int tripId
    ) {
        try {
            var tripDto = new TripDto();
            tripDto.setStars(star);
            tripService.update(tripDto, tripId);
            return ResponseEntity
                    .ok("Thanks for your star!");
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @PutMapping("/arrive")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<String> arriveDriver(@RequestParam int tripId) {
        try {
            var tripDto = new TripDto();
            tripDto.setArrivalTime(OffsetDateTime.now());
            tripService.update(tripDto, tripId);
            return ResponseEntity
                    .ok()
                    .body("Standby time 3 minutes");
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @PutMapping("/start")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<String> startTrip(@RequestParam int tripId) {
        try {
            var tripDto = new TripDto();
            tripDto.setStartTime(OffsetDateTime.now());
            tripService.update(tripDto, tripId);
            return ResponseEntity
                    .ok()
                    .body("Have a nice trip!");
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @PutMapping("/end")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<String> endTrip(@RequestParam int tripId) {
        try {
            var tripDto = new TripDto();
            tripDto.setEndTime(OffsetDateTime.now());
            tripService.update(tripDto, tripId);
            return ResponseEntity
                    .ok()
                    .body("Thank you for the trip");
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @GetMapping("/driverStatistic")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<TripResponse>> driverStatistic(
            @RequestParam String driverId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        ZoneId zone = ZoneId.of("Europe/Moscow");

        OffsetDateTime dateTimeFrom = LocalDateTime.parse(from, FORMATTER)
                .atZone(zone)
                .toOffsetDateTime();
        OffsetDateTime dateTimeTo = LocalDateTime.parse(to, FORMATTER)
                .atZone(zone)
                .toOffsetDateTime();

        return ResponseEntity.ok(tripService.getDriverStatistic(dateTimeFrom, dateTimeTo, driverId));
    }

    @GetMapping("/passengerStatistic")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<List<TripResponse>> passengerStatistic(
            @RequestParam String passengerId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        ZoneId zone = ZoneId.of("Europe/Moscow");

        OffsetDateTime dateTimeFrom = LocalDateTime.parse(from, FORMATTER)
                .atZone(zone)
                .toOffsetDateTime();
        OffsetDateTime dateTimeTo = LocalDateTime.parse(to, FORMATTER)
                .atZone(zone)
                .toOffsetDateTime();
        return ResponseEntity.ok(tripService.getPassengerStatistic(dateTimeFrom, dateTimeTo, passengerId));
    }
}
