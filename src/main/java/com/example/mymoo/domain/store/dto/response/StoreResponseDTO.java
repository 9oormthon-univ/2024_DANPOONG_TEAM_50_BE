package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Store;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data @Builder
public class StoreResponseDTO {
    private HttpStatus status;
    private String message;

    private static String createMessage(
            final HttpStatus status,
            final Store store,
            final String result){
        if (status == HttpStatus.NO_CONTENT) {
            return "id: "+ store.getId() +" name: "+ store.getName()  +" store has been updated " +" result: "+ result;
        }else if (status == HttpStatus.CREATED) {
            return "id: "+ store.getId() +" name: "+ store.getName()  +" store has been created " +" result: "+ result;
        }else{
            return "id: "+ store.getId() +" name: "+ store.getName()  +" store has been handled " +" result: "+ result;
        }
    }

    public static StoreResponseDTO from(
            final HttpStatus status,
            final Store store,
            final String result
    ) {
        return StoreResponseDTO.builder()
                .status(status)
                .message(createMessage(status, store, result))
                .build();
    }

}
