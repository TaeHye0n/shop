package shop.shop.controller.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.shop.common.response.Response;
import shop.shop.controller.item.dto.request.ItemRequestDto;
import shop.shop.exception.CustomAccessDeniedException;
import shop.shop.service.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/{memberId}")
    public ResponseEntity<Response<Long>> addItems (
            @PathVariable Long memberId,
            @RequestBody @Valid ItemRequestDto.ItemCreateRequest itemCreateRequest) throws CustomAccessDeniedException {
        return ResponseEntity.ok(
                Response.of(itemService.addItems(memberId, itemCreateRequest),
                "상품 등록 완료")
                );
    }

    @PostMapping("/{memberId}/{itemId}")
    public ResponseEntity<Response<Long>> updateItems (@PathVariable Long memberId,
                                       @PathVariable Long itemId,
                                       @RequestBody @Valid ItemRequestDto.ItemUpdateRequest itemUpdateRequest) throws CustomAccessDeniedException {
        return ResponseEntity.ok(
                Response.of(itemService.updateItem(memberId, itemId, itemUpdateRequest),
                        "상품 수정 완료")
        );
    }
}
