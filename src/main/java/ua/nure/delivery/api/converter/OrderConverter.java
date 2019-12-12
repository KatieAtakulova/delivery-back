package ua.nure.delivery.api.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.nure.delivery.api.dto.OrderDto;
import ua.nure.delivery.entity.Order;

import java.time.LocalDateTime;

@Component
public class OrderConverter implements Converter<OrderDto, Order> {

    @Override
    public Order convert(OrderDto orderDto) {

        Order order = new Order();

        order.setAddress(orderDto.getAddress());
        order.setDate(LocalDateTime.now());
        order.setDeliveryType(orderDto.getDeliveryType());
        order.setFullName(orderDto.getFullName());
        order.setPaymentType(orderDto.getPaymentType());
        order.setPhoneNumber(orderDto.getPhoneNumber());
        order.setStatus(orderDto.getStatus());

        return order;
    }
}
