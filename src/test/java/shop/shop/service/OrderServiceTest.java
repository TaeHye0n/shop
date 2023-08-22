package shop.shop.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import shop.shop.component.SecurityContextUtil;
import shop.shop.domain.entity.Member;
import shop.shop.domain.entity.Order;
import shop.shop.domain.entity.embeddable.Address;
import shop.shop.domain.entity.item.Item;
import shop.shop.domain.repository.item.ItemRepository;
import shop.shop.domain.repository.member.MemberRepository;
import shop.shop.domain.repository.order.OrderRepository;
import shop.shop.exception.CustomAccessDeniedException;
import shop.shop.exception.NotEnoughStockException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static shop.shop.controller.order.dto.request.OrderRequestDto.*;
import static shop.shop.controller.order.dto.request.OrderRequestDto.OrderCreateRequest.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Unit Test")
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private SecurityContextUtil securityContextUtil;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문성공")
    void 주문성공() throws Exception {
        //given
        Long memberId = 1L;
        Member member = createTestMember();
        ReflectionTestUtils.setField(member, "id", 1L);
        OrderCreateRequest orderCreateRequest = createTestOrderCreateRequest();
        Items items = createTestItems();
        ReflectionTestUtils.setField(items.item1(), "id", 1L);
        ReflectionTestUtils.setField(items.item2(), "id", 2L);
        given(securityContextUtil.getCurrentMember()).willReturn(member);
        given(itemRepository.findAllById(any())).willReturn(List.of(items.item1(), items.item2()));
        given(orderRepository.save(any(Order.class))).willAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            ReflectionTestUtils.setField(order, "id", 1L);
            return order;
        });

        //when
        Long savedOrderId = orderService.order(memberId, orderCreateRequest);

        //then
        Order orderFromOrderRepository = getOrderFromOrderRepository();
        assertThat(orderFromOrderRepository.getTotalPrice()).isEqualTo(25000);
        assertThat(savedOrderId).isEqualTo(1L);
        assertThat(items.item1().getStock()).isEqualTo(95);
        assertThat(items.item2().getStock()).isEqualTo(40);
        verify(securityContextUtil, times(1)).getCurrentMember();
        verify(itemRepository, times(1)).findAllById(any());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("주문 회원인증실패")
    void 주문인증실패() throws Exception {
        //given
        Long memberId = 1L;
        Member member = createTestMember();
        ReflectionTestUtils.setField(member, "id", 2L);
        OrderCreateRequest orderCreateRequest = createTestOrderCreateRequest();
        Items items = createTestItems();
        ReflectionTestUtils.setField(items.item1(), "id", 1L);
        ReflectionTestUtils.setField(items.item2(), "id", 2L);
        given(securityContextUtil.getCurrentMember()).willReturn(member);

        //when

        //then
        assertThrows(CustomAccessDeniedException.class, () -> orderService.order(memberId, orderCreateRequest));
        verify(securityContextUtil, times(1)).getCurrentMember();
    }

    @Test
    @DisplayName("없는상품 주문실패")
    void 주문실패() throws Exception {
        //given
        Long memberId = 1L;
        Member member = createTestMember();
        ReflectionTestUtils.setField(member, "id", 1L);
        OrderCreateRequest orderCreateRequest = createTestOrderCreateRequest();
        Items items = createTestItems();
        ReflectionTestUtils.setField(items.item1(), "id", 1L);
        ReflectionTestUtils.setField(items.item2(), "id", 3L);
        given(securityContextUtil.getCurrentMember()).willReturn(member);
        given(itemRepository.findAllById(any())).willReturn(List.of(items.item1(), items.item2()));

        //when

        //then
        assertThrows(EntityNotFoundException.class, () -> orderService.order(memberId, orderCreateRequest));
        verify(securityContextUtil, times(1)).getCurrentMember();
        verify(itemRepository, times(1)).findAllById(any());
    }

    @Test
    @DisplayName("주문수량부족")
    void 주문수량부족() throws Exception {
        //given
        Long memberId = 1L;
        Member member = createTestMember();
        ReflectionTestUtils.setField(member, "id", 1L);
        OrderCreateRequest orderCreateRequest = createTestOrderCreateRequest();
        Items items = createTestItems();
        items.item1.changeStock(1);
        ReflectionTestUtils.setField(items.item1(), "id", 1L);
        ReflectionTestUtils.setField(items.item2(), "id", 2L);
        given(securityContextUtil.getCurrentMember()).willReturn(member);
        given(itemRepository.findAllById(any())).willReturn(List.of(items.item1(), items.item2()));

        //when

        //then
        assertThrows(NotEnoughStockException.class, () -> orderService.order(memberId, orderCreateRequest));
        verify(securityContextUtil, times(1)).getCurrentMember();
        verify(itemRepository, times(1)).findAllById(any());
    }

    private Items createTestItems() {
        Item item1 = Item.builder()
                .stock(100)
                .price(1000)
                .build();
        Item item2 = Item.builder()
                .price(2000)
                .stock(50)
                .build();
        Items items = new Items(item1, item2);
        return items;
    }

    private record Items(Item item1, Item item2) {
    }

    private OrderCreateRequest createTestOrderCreateRequest() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        ItemAndCount itemAndCount1 = ItemAndCount.builder()
                .itemId(1L)
                .count(5)
                .build();
        ItemAndCount itemAndCount2 = ItemAndCount.builder()
                .itemId(2L)
                .count(10)
                .build();
        orderCreateRequest.setItemAndCounts(List.of(itemAndCount1, itemAndCount2));
        return orderCreateRequest;
    }

    private Member createTestMember() {
        Member member = Member.builder()
                .address(new Address("test", "test2", "test3"))
                .build();
        return member;
    }

    private Order getOrderFromOrderRepository() {
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        return orderCaptor.getValue();
    }
}