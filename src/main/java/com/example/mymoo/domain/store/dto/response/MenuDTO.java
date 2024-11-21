package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Menu;
import lombok.Builder;
import lombok.Data;

@Data
public class MenuDTO {
    private Long id;
    private String name;
    private String imagePath;
    private String description;
    private int price;

    @Builder
    public MenuDTO(String name, String imagePath, String description, int price) {
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
        this.price = price;
    }

    public Menu toEntity(){
        return Menu.builder()
                .name(name)
                .imagePath(imagePath)
                .description(description)
                .price(price)
                .build();
    }
}
