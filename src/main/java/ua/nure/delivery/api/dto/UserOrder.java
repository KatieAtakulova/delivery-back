package ua.nure.delivery.api.dto;

import lombok.Builder;
import lombok.Data;
import ua.nure.delivery.entity.Product;
import ua.nure.delivery.entity.enums.DeliveryType;
import ua.nure.delivery.entity.enums.OrderState;
import ua.nure.delivery.entity.enums.OrderStatus;
import ua.nure.delivery.entity.enums.PaymentType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserOrder {

    private Long id;

    private String fullName;

    private String address;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    private String fullPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    private LocalDateTime date;

    private List<Product> products;

    @Builder
    public UserOrder(String fullName, String address, String phoneNumber, PaymentType paymentType,
                     DeliveryType deliveryType, String fullPrice, OrderStatus status, OrderState state,
                     LocalDateTime date, List<Product> products) {
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.paymentType = paymentType;
        this.deliveryType = deliveryType;
        this.fullPrice = fullPrice;
        this.status = status;
        this.state = state;
        this.date = date;
        this.products = products;
    }
}
