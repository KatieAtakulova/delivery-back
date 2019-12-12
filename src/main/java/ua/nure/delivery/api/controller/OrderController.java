package ua.nure.delivery.api.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.delivery.api.dto.OrderDto;
import ua.nure.delivery.api.dto.UserOrder;
import ua.nure.delivery.entity.Order;
import ua.nure.delivery.entity.enums.DeliveryType;
import ua.nure.delivery.entity.enums.OrderState;
import ua.nure.delivery.entity.enums.OrderStatus;
import ua.nure.delivery.entity.enums.PaymentType;
import ua.nure.delivery.service.OrderService;
import ua.nure.delivery.service.convert.ExtendedConversionService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderController {

    private ExtendedConversionService conversionService;
    private OrderService orderService;

    public OrderController(ExtendedConversionService conversionService, OrderService orderService) {
        this.conversionService = conversionService;
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public void createOrder(@RequestBody OrderDto orderDto) {

        Order order = conversionService.convert(orderDto, Order.class);
        orderService.createOrder(order, orderDto.getProductIds());
        System.out.println();
    }

    @PutMapping("/update")
    public void update(@RequestBody OrderDto orderDto, @RequestParam Long orderId){
        Order order = conversionService.convert(orderDto, Order.class);
        orderService.update(orderId, order, orderDto.getProductIds());
    }

    @PutMapping("/change-delivery-state")
    public void changeDeliveryStatus(Long orderId, OrderState orderState) {
        orderService.changeOrderState(orderId, orderState);
    }

    @PutMapping("/change-order-status")
    public void changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        orderService.changeOrderStatus(orderId, orderStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @ApiOperation("Retrieve information about user orders")
    @GetMapping("/all-orders")
    public ResponseEntity<List<UserOrder>> findAllUserOrders(
            @RequestParam(required = false) PaymentType paymentType,
            @RequestParam(required = false) DeliveryType deliveryType,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) OrderState state,
            @RequestParam(required = false) Long id
    ) {
        List<Order> allUserOrders = orderService.findAllUserOrders(paymentType, deliveryType, state, status, id);
        return ResponseEntity.ok(conversionService.convertAll(allUserOrders, UserOrder.class));
    }
}
