package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Menu;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
public class MenuListDTO {

    long total_count;
    List<MenuDetailDTO> menus;

    public static MenuListDTO from(List<Menu> menus) {
        return MenuListDTO.builder()
                .total_count(menus.size())
                .menus(menus.stream().map(MenuDetailDTO::from).toList())
                .build();
    }

    @Data @Builder
    public static class MenuDetailDTO{
        private Long id;
        private String name;
        private String imagePath;
        private String description;
        private int price;

        public static MenuDetailDTO from(Menu menu) {
            return MenuDetailDTO.builder()
                    .id(menu.getId())
                    .name(menu.getName())
                    .imagePath(menu.getImagePath())
                    .description(menu.getDescription())
                    .price(menu.getPrice())
                    .build();
        }
    }
}
