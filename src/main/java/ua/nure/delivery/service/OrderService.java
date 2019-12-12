package ua.nure.delivery.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.plugin.dom.exception.InvalidStateException;
import ua.nure.delivery.entity.Order;
import ua.nure.delivery.entity.OrderProduct;
import ua.nure.delivery.entity.Product;
import ua.nure.delivery.entity.User;
import ua.nure.delivery.entity.enums.DeliveryType;
import ua.nure.delivery.entity.enums.OrderState;
import ua.nure.delivery.entity.enums.OrderStatus;
import ua.nure.delivery.entity.enums.PaymentType;
import ua.nure.delivery.repo.OrderRepository;
import ua.nure.delivery.repo.ProductRepository;
import ua.nure.delivery.repo.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private ProductRepository productRepository;
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    private ValidationService validationService;
    private UserService userService;

    public OrderService(ProductRepository productRepository, UserRepository userRepository,
                        OrderRepository orderRepository, ValidationService validationService, UserService userService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.validationService = validationService;
        this.userService = userService;
    }

    @Transactional
    public void createOrder(Order order, List<Long> productsIds) {

        List<Product> orderProducts = productRepository.findAllById(productsIds);

        order.setFullPrice(calculateFullPrice(orderProducts));

        List<OrderProduct> keys = orderProducts.stream()
                .map(product -> new OrderProduct(null, order, product))
                .collect(Collectors.toList());

        order.setOrderProducts(keys);

        order.setState(OrderState.CARGO_AND_CONTAINER_INTACT);

        orderProducts.forEach(product -> product.setOrderProducts(keys));

        order.setUser(userService.getCurrentUser());

        orderRepository.save(order);
    }

    public List<Order> findAllUserOrders(PaymentType paymentType, DeliveryType deliveryType, OrderState state, OrderStatus status, Long id) {

        User user = userService.getCurrentUser();

        Specification<Order> orderSpecification;

        if (id != null) {
            validationService.validateAdminOrModer();
            orderSpecification = Specification
                    .where((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), id));
        } else {
            orderSpecification = Specification
                    .where((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), user.getId()));
        }

        if (paymentType != null) {
            orderSpecification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentType"), paymentType));
        }
        if (deliveryType != null) {
            orderSpecification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("deliveryType"), deliveryType));
        }
        if (status != null) {
            orderSpecification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
        }
        if (state != null) {
            orderSpecification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("state"), state));
        }

        return orderRepository.findAll(orderSpecification);
    }

    public void update(Long orderId, Order updateOrder, List<Long> productIds) {

        validationService.validateEntity(orderId, Order.class);

        Order order = orderRepository.findById(orderId).get();

        if (order.getStatus().equals(OrderStatus.IN_FILLING_PROCESS)) {

            updateOrder.setId(order.getId());
            updateOrder.setUser(order.getUser());

            List<Product> orderProducts = productRepository.findAllById(productIds);
            order.setFullPrice(calculateFullPrice(orderProducts));

            List<OrderProduct> keys = orderProducts.stream()
                    .map(product -> new OrderProduct(null, order, product))
                    .collect(Collectors.toList());

            updateOrderProducts(order, keys);

            updateOrder.setOrderProducts(keys);

            orderProducts.forEach(product -> product.setOrderProducts(keys));

            orderRepository.save(updateOrder);
        } else {
            throw new InvalidStateException("You cannot change the order parameters because the order has passed the status 'IN_FILLING_PROCESS'");
        }
    }

    private void updateOrderProducts(Order order, List<OrderProduct> keys) {
        order.getOrderProducts().forEach(orderProduct -> {
            Optional<OrderProduct> existsProduct = keys.stream()
                    .filter(key -> key.getProduct().getProductId().equals(orderProduct.getProduct().getProductId()))
                    .findFirst();
            if (existsProduct.isPresent()) {
                Product product = existsProduct.get().getProduct();
                product.setId(orderProduct.getProduct().getId());
            }
        });
    }

    public void changeOrderState(Long orderId, OrderState orderState) {

        validationService.validateEntity(orderId, Order.class);

        Order order = orderRepository.findById(orderId).get();
        order.setState(orderState);

        orderRepository.save(order);
    }

    public void changeOrderStatus(Long orderId, OrderStatus orderStatus) {

        validationService.validateEntity(orderId, Order.class);

        Order order = orderRepository.findById(orderId).get();
        order.setStatus(orderStatus);

        orderRepository.save(order);
    }

    private String calculateFullPrice(List<Product> orderProducts) {
        return String.valueOf(orderProducts.stream()
                .mapToDouble(Product::getPrice)
                .sum());
    }

    public Order getOrderById(Long id) {
        validationService.validateEntity(id, Order.class);
        return orderRepository.findById(id).get();
    }
}
