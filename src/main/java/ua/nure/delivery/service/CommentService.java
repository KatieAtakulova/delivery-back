package ua.nure.delivery.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ua.nure.delivery.entity.Comment;
import ua.nure.delivery.entity.Order;
import ua.nure.delivery.entity.enums.OrderStatus;
import ua.nure.delivery.repo.CommentRepository;
import ua.nure.delivery.repo.OrderRepository;

import java.util.List;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private OrderRepository orderRepository;

    private ValidationService validationService;

    public CommentService(CommentRepository commentRepository, OrderRepository orderRepository, ValidationService validationService) {
        this.commentRepository = commentRepository;
        this.orderRepository = orderRepository;
        this.validationService = validationService;
    }

    public void saveComment(Long orderId, String text, Integer rating) {

        validationService.validateEntity(orderId, Order.class);

        if (!orderRepository.findById(orderId).get().getStatus().equals(OrderStatus.EXECUTED)) {
            throw new IllegalArgumentException("The user must pay for the purchase before leaving a review");
        }
        if ((rating < 0 || rating > 5) || StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("The text of the comment must not be empty and the rating must range from 0 to 5");
        }
        commentRepository.save(new Comment(text, rating));
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}
