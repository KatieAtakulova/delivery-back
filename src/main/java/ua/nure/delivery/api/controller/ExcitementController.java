package ua.nure.delivery.api.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.delivery.service.ExcitementService;

@RestController
@RequestMapping(value = "/api/v1/excitements")
public class ExcitementController {

    private ExcitementService excitementService;

    public ExcitementController(ExcitementService excitementService) {
        this.excitementService = excitementService;
    }

    @ApiOperation("Create excitement")
    @PostMapping("/create")
    public void createExcitement(Long orderId, String address, String text) {
        excitementService.saveExcitement(orderId, address, text);
    }
}
