package com.taxi.repository;

import com.taxi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Transactional
    @Modifying
    @Query(value = "insert into public.order(passenger_id, order_time, start_point, end_point, cost, status) values " +
            "(:passengerId," +
            ":orderTime," +
            "ST_SetSRID(ST_Point( :stLat, :stLon ), 4326)\\:\\:geography," +
            "ST_SetSRID(ST_Point( :endLat, :endLon ), 4326)\\:\\:geography," +
            ":cost, :status)", nativeQuery = true)
    void save(String passengerId, OffsetDateTime orderTime, Double stLat, Double stLon,
              Double endLat, Double endLon, Integer cost, String status);
}
