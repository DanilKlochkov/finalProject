package com.taxi.repository;

import com.taxi.entity.Driver;
import com.taxi.entity.Passenger;
import com.taxi.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Integer> {
    @Query("SELECT t FROM Trip t WHERE t.endTime > :endTimeLess AND t.endTime < :endTimeMore AND t.driver = :driver")
    List<Trip> findByTimeAndDriverId(@Param("endTimeLess") OffsetDateTime endTimeLess,
                                     @Param("endTimeMore") OffsetDateTime endTimeMore,
                                     @Param("driver") Driver driver);

    @Query("SELECT t FROM Trip t WHERE t.endTime > :endTimeLess AND t.endTime < :endTimeMore " +
            "AND t.order.passenger = :passenger")
    List<Trip> findByTimeAndPassengerId(@Param("endTimeLess") OffsetDateTime endTimeLess,
                               @Param("endTimeMore") OffsetDateTime endTimeMore,
                               @Param("passenger") Passenger passenger);


}
