package shop.shop.service;

import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import shop.shop.component.SecurityContextUtil;
import shop.shop.controller.item.dto.request.ItemRequestDto;
import shop.shop.domain.entity.Member;
import shop.shop.domain.entity.enums.Quality;
import shop.shop.domain.entity.item.Item;
import shop.shop.domain.repository.item.ItemRepository;
import shop.shop.domain.repository.member.MemberRepository;
import shop.shop.exception.CustomAccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static shop.shop.controller.item.dto.request.ItemRequestDto.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemService Unit Test")
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SecurityContextUtil securityContextUtil;
    
    @Test
    @DisplayName("상품등록 성공")
    void 상품등록성공() throws Exception {
        //given
        Long memberId = 1L;
        ItemCreateRequest request = ItemCreateRequest.builder()
                .name("Test")
                .quality(Quality.NEW)
                .price(10000)
                .stock(100)
                .build();
        Item item = request.toEntity();
        Member member = Member.builder()
                .email("test@test.com")
                .password("test123")
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(item, "id", 1L);

        //when
        when(securityContextUtil.getCurrentMember()).thenReturn(member);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Long itemId = itemService.addItems(memberId, request);

        //then
        assertThat(itemId).isEqualTo(1L);
        verify(securityContextUtil, times(1)).getCurrentMember();
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("상품등록 인증실패")
    void 상품등록인증실패() throws Exception {
        //given
        Long memberId = 1L;
        ItemCreateRequest request = ItemCreateRequest.builder()
                .name("Test")
                .quality(Quality.NEW)
                .price(10000)
                .stock(100)
                .build();
        Item item = request.toEntity();
        Member member = Member.builder()
                .email("test@test.com")
                .password("test123")
                .build();
        ReflectionTestUtils.setField(member, "id", 2L);
        ReflectionTestUtils.setField(item, "id", 1L);

        //when
        when(securityContextUtil.getCurrentMember()).thenReturn(member);

        //then
        assertThrows(CustomAccessDeniedException.class, () -> itemService.addItems(memberId, request));
        verify(securityContextUtil, times(1)).getCurrentMember();
    }
    
    @Test
    @DisplayName("상품수정 성공")
    void 상품수정성공() throws Exception {
        //given
        Long memberId = 1L;
        Long itemId = 1L;
        Item item = ItemCreateRequest.builder()
                .name("Test")
                .quality(Quality.NEW)
                .price(10000)
                .stock(100)
                .build().toEntity();
        Member member = Member.builder()
                .email("test@test.com")
                .password("test123")
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(item, "id", 1L);
        ItemUpdateRequest request = ItemUpdateRequest.builder()
                .name("Test2")
                .quality(Quality.D)
                .stock(1)
                .build();

        //when
        when(securityContextUtil.getCurrentMember()).thenReturn(member);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Long savedItemId = itemService.updateItem(memberId, itemId, request);

        //then
        assertThat(savedItemId).isEqualTo(itemId);
        assertThat(item.getName()).isEqualTo("Test2");
        assertThat(item.getPrice()).isEqualTo(10000);
        verify(securityContextUtil, times(1)).getCurrentMember();
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("상품수정 인증실패")
    void 상품수정인증실패() throws Exception {
        //given
        Long memberId = 1L;
        Long itemId = 1L;
        Item item = ItemCreateRequest.builder()
                .name("Test")
                .quality(Quality.NEW)
                .price(10000)
                .stock(100)
                .build().toEntity();
        Member member = Member.builder()
                .email("test@test.com")
                .password("test123")
                .build();
        ReflectionTestUtils.setField(member, "id", 2L);
        ReflectionTestUtils.setField(item, "id", 1L);
        ItemUpdateRequest request = ItemUpdateRequest.builder()
                .name("Test2")
                .quality(Quality.D)
                .stock(1)
                .build();

        //when
        when(securityContextUtil.getCurrentMember()).thenReturn(member);

        //then
        assertThrows(CustomAccessDeniedException.class, () -> itemService.updateItem(memberId, itemId, request));
        verify(securityContextUtil, times(1)).getCurrentMember();
    }

    @Test
    @DisplayName("상품수정 실패")
    void 상품수정실패() throws Exception {
        //given
        Long memberId = 1L;
        Long itemId = 1L;
        Item item = ItemCreateRequest.builder()
                .name("Test")
                .quality(Quality.NEW)
                .price(10000)
                .stock(100)
                .build().toEntity();
        Member member = Member.builder()
                .email("test@test.com")
                .password("test123")
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(item, "id", 1L);
        ItemUpdateRequest request = ItemUpdateRequest.builder()
                .name("Test2")
                .quality(Quality.D)
                .stock(1)
                .build();

        //when
        when(securityContextUtil.getCurrentMember()).thenReturn(member);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(memberId, itemId, request));
        verify(securityContextUtil, times(1)).getCurrentMember();
        verify(itemRepository, times(1)).findById(anyLong());
    }


}