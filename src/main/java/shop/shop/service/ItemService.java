package shop.shop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.shop.component.SecurityContextUtil;
import shop.shop.domain.entity.Member;
import shop.shop.domain.entity.item.Item;
import shop.shop.domain.repository.item.ItemRepository;
import shop.shop.domain.repository.member.MemberRepository;
import shop.shop.exception.CustomAccessDeniedException;

import static shop.shop.controller.item.dto.request.ItemRequestDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final SecurityContextUtil securityContextUtil;

    @Transactional
    public Long addItems(Long memberId, ItemCreateRequest itemCreateRequest) throws CustomAccessDeniedException {
        Member member = securityContextUtil.getCurrentMember();
        if (member.getId().equals(memberId)) {
            Item item = itemCreateRequest.toEntity();
            item.setMember(member);
            Item savedItem = itemRepository.save(item);
            return savedItem.getId();
        } else throw new CustomAccessDeniedException("Access Denied");
    }

    @Transactional
    public Long updateItem(Long memberId, Long itemId, ItemUpdateRequest itemUpdateRequest) throws CustomAccessDeniedException {
        Member member = securityContextUtil.getCurrentMember();
        if (member.getId().equals(memberId)) {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("Item not found"));

            if (itemUpdateRequest.getName() != null) {
                item.changeName(itemUpdateRequest.getName());
            }
            if (itemUpdateRequest.getPrice() != 0) {
                item.changePrice(itemUpdateRequest.getPrice());
            }
            if (itemUpdateRequest.getStock() != 0) {
                item.changeStock(itemUpdateRequest.getStock());
            }
            if (itemUpdateRequest.getQuality() != null) {
                item.changeQuality(itemUpdateRequest.getQuality());
            }

            // 변경사항이 있을 때만 저장
            if (itemUpdateRequest.getName() != null || itemUpdateRequest.getPrice() != 0 ||
                    itemUpdateRequest.getStock() != 0 || itemUpdateRequest.getQuality() != null) {
                itemRepository.save(item);
            }
            return item.getId();
        } else throw new CustomAccessDeniedException("Access Denied");
    }


}
