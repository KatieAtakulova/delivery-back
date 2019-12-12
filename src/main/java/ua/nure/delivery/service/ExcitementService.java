package ua.nure.delivery.service;

import org.springframework.stereotype.Service;
import ua.nure.delivery.entity.Excitement;
import ua.nure.delivery.entity.Order;
import ua.nure.delivery.entity.enums.OrderStatus;
import ua.nure.delivery.repo.ExcitementRepository;
import ua.nure.delivery.repo.OrderRepository;

@Service
public class ExcitementService {

    private OrderRepository orderRepository;
    private ExcitementRepository excitementRepository;

    private ValidationService validationService;

    public ExcitementService(OrderRepository orderRepository, ExcitementRepository excitementRepository, ValidationService validationService) {
        this.orderRepository = orderRepository;
        this.excitementRepository = excitementRepository;
        this.validationService = validationService;
    }

    public void saveExcitement(Long orderId, String address, String text) {

        validationService.validateExcitement(orderId, Order.class, text, address);

        Order order = orderRepository.findById(orderId).get();

        Excitement excitement = Excitement.builder()
                .address(address)
                .text(text)
                .order(order)
                .build();
        excitementRepository.save(excitement);

        order.setStatus(OrderStatus.RETURN_IN_TRANSIT);
        orderRepository.save(order);
    }
}
