package com.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taxi.enums.OrderStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @Column(name = "order_time", nullable = false)
    private OffsetDateTime orderTime = OffsetDateTime.now();

    @JsonIgnore
    @Column(name = "start_point", nullable = false, columnDefinition = "geometry(Point,4326)")
    private Geometry startPoint;

    @JsonIgnore
    @Column(name = "end_point", nullable = false, columnDefinition = "geometry(Point,4326)")
    private Geometry endPoint;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "status")
    private String status = String.valueOf(OrderStatusEnum.WAITING);
}