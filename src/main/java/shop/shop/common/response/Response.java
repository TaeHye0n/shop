package shop.shop.common.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    private T data;

    private int status;

    private String message;

    public static Response of(String message) {
        return Response.builder()
                .status(200)
                .message(message)
                .build();
    }

    public static <T> Response<T> of(T data, String message) {
        return (Response<T>) Response.builder()
                .status(200)
                .data(data)
                .message(message)
                .build();
    }

}
