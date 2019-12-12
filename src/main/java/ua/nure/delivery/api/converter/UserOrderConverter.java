package ua.nure.delivery.api.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.nure.delivery.api.dto.UserOrder;
import ua.nure.delivery.entity.Order;
import ua.nure.delivery.entity.OrderProduct;
import ua.nure.delivery.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserOrderConverter implements Converter<Order, UserOrder> {

    @Override
    public UserOrder convert(Order order) {

        List<Product> products = order.getOrderProducts().stream()
                .map(OrderProduct::getProduct)
                .collect(Collectors.toList());

        return UserOrder.builder()
                .address(order.getAddress())
                .date(order.getDate())
                .deliveryType(order.getDeliveryType())
                .fullName(order.getFullName())
                .fullPrice(order.getFullPrice())
                .paymentType(order.getPaymentType())
                .phoneNumber(order.getPhoneNumber())
                .state(order.getState())
                .status(order.getStatus())
                .products(products)
                .build();
    }
}
