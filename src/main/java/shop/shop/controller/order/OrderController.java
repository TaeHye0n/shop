package shop.shop.controller.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.shop.controller.order.dto.request.OrderRequestDto;
import shop.shop.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity order (@RequestBody @Valid OrderRequestDto.OrderCreateRequest orderCreateRequest) {
        return new ResponseEntity(orderService.order(orderCreateRequest), HttpStatus.CREATED);
    }
}
