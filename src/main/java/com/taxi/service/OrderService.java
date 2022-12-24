package com.taxi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.taxi.dto.OrderResponse;
import com.taxi.dto.OrderToCreate;
import com.taxi.entity.Order;
import com.taxi.enums.OrderStatusEnum;
import com.taxi.jwt.util.MapUtil;
import com.taxi.repository.OrderRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MapUtil mapUtil;
    private final JdbcTemplate jdbcTemplate;
    private static final Integer COST = 15;

    public void save(String passengerId, OrderToCreate order) throws IOException {
        var coordinateFrom = mapUtil.toGeoCode(order.getAddressFrom());
        var coordinateTo = mapUtil.toGeoCode(order.getAddressTo());
        var price = Integer.parseInt(mapUtil.getDistance(coordinateFrom + "|" + coordinateTo)) / 1000 * COST + 50;

        var coordinateFromTuple = coordinateFrom.split(",");
        var coordinateToTuple = coordinateTo.split(",");

        orderRepository.save(passengerId, OffsetDateTime.now(), Double.valueOf(coordinateFromTuple[0]), Double.valueOf(coordinateFromTuple[1]),
                Double.valueOf(coordinateToTuple[0]), Double.valueOf(coordinateToTuple[1]), price, String.valueOf(OrderStatusEnum.WAITING));
    }

    public void updateStatus(OrderStatusEnum status, int orderId) throws NotFoundException {
        if (orderRepository.findById(orderId).isEmpty()) {
            throw new NotFoundException("order with id = " + orderId + " not found!");
        }
        Order updateOrder = orderRepository.findById(orderId).get();
        if (Objects.nonNull(status)) {
            updateOrder.setStatus(String.valueOf(status));
        }
        orderRepository.save(updateOrder);
    }

    public List<OrderResponse> findAllWaitingOrders() {
        return orderRepository
                .findAll()
                .stream()
                .filter(x -> x.getStatus().equals(String.valueOf(OrderStatusEnum.WAITING)))
                .map(x -> {
                    try {
                        return orderToResponse(x);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    public Order findOrderById(int id) {
        return orderRepository.findById(id).get();
    }

    public String getCoordinatesFrom(int id) {
        var start = jdbcTemplate.queryForObject("select ST_AsGeoJSON(start_point) from public.order WHERE id = ?", String.class, id);
        return escapeFromBrackets(start);
    }

    private String getCoordinatesTo(int id) {
        var end = jdbcTemplate.queryForObject("select ST_AsGeoJSON(end_point) from public.order WHERE id = ?", String.class, id);
        return escapeFromBrackets(end);
    }

    private String escapeFromBrackets(String in) {
        Pattern p = Pattern.compile("\\[(.*)\\]");
        Matcher m = p.matcher(in);
        StringBuilder s = new StringBuilder();
        while (m.find()) {
            s.append(m.group(1));
        }
        return s.toString();
    }

    private OrderResponse orderToResponse(Order order) throws JsonProcessingException {
        return OrderResponse.builder()
                .passenger(order.getPassenger().getUserName())
                .orderTime(order.getOrderTime())
                .addressFrom(
                        mapUtil.toAddress(getCoordinatesFrom(order.getId()))
                )
                .addressTo(
                        mapUtil.toAddress(getCoordinatesTo(order.getId()))
                )
                .cost(order.getCost())
                .status(order.getStatus())
                .build();
    }
}
