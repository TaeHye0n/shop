package shop.shop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.shop.component.SecurityContextUtil;
import shop.shop.domain.entity.Delivery;
import shop.shop.domain.entity.Member;
import shop.shop.domain.entity.Order;
import shop.shop.domain.entity.OrderItemMap;
import shop.shop.domain.entity.enums.DeliveryStatus;
import shop.shop.domain.entity.item.Item;
import shop.shop.domain.repository.item.ItemRepository;
import shop.shop.domain.repository.member.MemberRepository;
import shop.shop.domain.repository.order.OrderRepository;
import shop.shop.exception.CustomAccessDeniedException;
import shop.shop.exception.CustomAuthenticationException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static shop.shop.controller.order.dto.request.OrderRequestDto.*;
import static shop.shop.controller.order.dto.request.OrderRequestDto.OrderCreateRequest.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final SecurityContextUtil securityContextUtil;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, OrderCreateRequest orderCreateRequest) throws CustomAccessDeniedException {

        Member member = securityContextUtil.getCurrentMember();

        if (member.getId().equals(memberId)) {
            Map<Long, Integer> itemIdAndCount = orderCreateRequest.getItemAndCounts().stream()
                    .collect(Collectors.toMap(ItemAndCount::getItemId, ItemAndCount::getCount));

            List<Long> itemIdList = itemIdAndCount.keySet().stream().toList();

            Map<Long, Item> findItemMap = itemRepository.findAllById(itemIdList).stream()
                    .collect(Collectors.toMap(Item::getId, i -> i));

            Delivery delivery = Delivery.builder()
                    .address(member.getAddress())
                    .deliveryStatus(DeliveryStatus.READY)
                    .build();

            List<OrderItemMap> orderItemMapList = new ArrayList<>();

            for (ItemAndCount itemAndCount : orderCreateRequest.getItemAndCounts()) {
                Item item = findItemMap.get(itemAndCount.getItemId());
                if (item == null) throw new EntityNotFoundException("Item not found");
                int count = itemIdAndCount.get(itemAndCount.getItemId());
                orderItemMapList.add(OrderItemMap.createOrderItemMap(item, item.getPrice(), count));
            }

            Order order = Order.createOrder(member, delivery, orderItemMapList);
            orderRepository.save(order);

            return order.getId();
        } else throw new CustomAccessDeniedException("Access denied");
    }
}
