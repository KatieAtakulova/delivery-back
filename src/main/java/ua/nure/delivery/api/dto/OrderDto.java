package ua.nure.delivery.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ua.nure.delivery.entity.enums.DeliveryType;
import ua.nure.delivery.entity.enums.OrderState;
import ua.nure.delivery.entity.enums.OrderStatus;
import ua.nure.delivery.entity.enums.PaymentType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
public class OrderDto {

    private String fullName;

    private String address;

    private String phoneNumber;

    @ApiModelProperty(allowableValues = "PAY_PASS, PAY_PAl, GOOGLE_PAY, MASTER_PASS")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @ApiModelProperty(allowableValues = "WATER, AIR, EARTH")
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    private String fullPrice;

    @ApiModelProperty(allowableValues = "IN_FILLING_PROCESS, EXECUTED, IN_TRANSIT, DELIVERED, RETURN_IN_TRANSIT, RETURN_COMPLETED")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private List<Long> productIds;
}
