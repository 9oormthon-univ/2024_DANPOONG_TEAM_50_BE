package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Store;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class StoreResponseDTO {
    private HttpStatus status;
    private String message;

    public StoreResponseDTO(HttpStatus status, Store store, String result) {
        this.status = status;
        if (status == HttpStatus.NO_CONTENT) {
            this.message = "id: "+ store.getId() +" name: "+ store.getName()  +" store has been updated " +" result: "+ result;
        }else if (status == HttpStatus.CREATED) {
            this.message = "id: "+ store.getId() +" name: "+ store.getName()  +" store has been created " +" result: "+ result;
        }else{
            this.message = "id: "+ store.getId() +" name: "+ store.getName()  +" store has been handled " +" result: "+ result;
        }
    }
}
