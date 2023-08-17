package shop.shop.controller.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.shop.controller.item.dto.request.ItemRequestDto;
import shop.shop.exception.CustomAccessDeniedException;
import shop.shop.service.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/{memberId}")
    public ResponseEntity addItems (
            @PathVariable Long memberId,
            @RequestBody @Valid ItemRequestDto.ItemCreateRequest itemCreateRequest) throws CustomAccessDeniedException {
        return new ResponseEntity(itemService.addItems(memberId, itemCreateRequest), HttpStatus.CREATED);
    }

    @PostMapping("/{memberId}/{itemId}")
    public ResponseEntity updateItems (@PathVariable Long memberId,
                                       @PathVariable Long itemId,
                                       @RequestBody @Valid ItemRequestDto.ItemUpdateRequest itemUpdateRequest) throws CustomAccessDeniedException {
        return new ResponseEntity(itemService.updateItem(memberId, itemId, itemUpdateRequest), HttpStatus.OK);
    }
}
