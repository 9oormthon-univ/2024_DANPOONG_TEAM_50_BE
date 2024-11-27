package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Store;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class StoreDetailDTO {

    private Long id;
    private String name;
    private String address;
    private String imagePath;
    private Double stars;
    private Integer likeCount;
    private Long allDonation;
    private Long usableDonation;
    private boolean likeable;

    public static StoreDetailDTO from(Store store, boolean likeable){
        return StoreDetailDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .imagePath(store.getImagePath())
                .stars(store.getStars())
                .likeCount(store.getLikeCount())
                .allDonation(store.getAllDonation())
                .usableDonation(store.getUsableDonation())
                .likeable(likeable)
                .build();
    }
}
