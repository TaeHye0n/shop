package shop.shop.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

	// stomp 방식으로 메세지를 주고 받기 위해 의존성 주입
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;
    
    
	// 메세지 발행 시 매핑 되는 주소 (클라이언트 측에서는 /pub/chat 라는 주소 메세지를 보내면 된다.)
    @MessageMapping("/chat")
    public void message(Message message) {
        chatService.saveMessage(message);
        simpMessageSendingOperations.convertAndSend("/sub/" + message.getChannelId(), message); // 구독한 주소에게 메세지를 발행함
    }
}