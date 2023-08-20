package shop.shop.controller.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.shop.controller.order.dto.request.OrderRequestDto;
import shop.shop.exception.CustomAccessDeniedException;
import shop.shop.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{memberId}")
    public ResponseEntity order (
            @PathVariable Long memberId,
            @RequestBody @Valid OrderRequestDto.OrderCreateRequest orderCreateRequest) throws CustomAccessDeniedException {
        return new ResponseEntity(orderService.order(memberId, orderCreateRequest), HttpStatus.CREATED);
    }
}
