package ua.nure.delivery.api.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.delivery.entity.Comment;
import ua.nure.delivery.service.CommentService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/comments")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation("Create comment")
    @PostMapping("/create")
    public void createExcitement(Long orderId, String text, Integer rating) {
        commentService.saveComment(orderId, text, rating);
    }

    @ApiOperation("Retrieve all comments")
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }
}
