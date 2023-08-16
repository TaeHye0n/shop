package shop.shop.controller.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.shop.controller.item.dto.request.ItemRequestDto;
import shop.shop.service.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity addItems (@RequestBody @Valid ItemRequestDto.ItemCreateRequest itemCreateRequest) {
        return new ResponseEntity(itemService.addItems(itemCreateRequest), HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity updateItems (@PathVariable Long id,
                                       @RequestBody @Valid ItemRequestDto.ItemUpdateRequest itemUpdateRequest) {
        return new ResponseEntity(itemService.updateItem(id, itemUpdateRequest), HttpStatus.OK);
    }
}
