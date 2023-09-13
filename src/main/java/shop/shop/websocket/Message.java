package shop.shop.websocket;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chatting")
public class Message<T> {

    @Id
    private String id;
    private String channelId;
    private String name;
}
