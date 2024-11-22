package com.example.mymoo.domain.child.dto.response;

import com.example.mymoo.domain.child.entity.Child;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ChildResponseDTO {
    private HttpStatus status;
    private String message;

    public ChildResponseDTO(HttpStatus status, Child child, String result) {
        this.status = status;
        if (status == HttpStatus.NO_CONTENT) {
            this.message = "id: "+ child.getId() +" child has been updated " +" result: "+ result;
        }else if (status == HttpStatus.CREATED) {
            this.message = "id: "+ child.getId()  +" child has been created " +" result: "+ result;
        }else{
            this.message = "id: "+ child.getId()  +" child has been handled " +" result: "+ result;
        }
    }
}
