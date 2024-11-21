package com.example.mymoo.domain.store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_MENU")
@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String imagePath;
    private String description;
    private int price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Menu(String name, String imagePath, String description, int price, Store store) {
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
        this.price = price;
        this.store = store;
    }

    public void setStore(Store store){
        this.store = store;
    }
}
