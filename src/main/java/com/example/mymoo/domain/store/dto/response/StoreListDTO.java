package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Like;
import com.example.mymoo.domain.store.entity.Store;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
public class StoreListDTO {
    private long totalCount;
    private int page;
    private int size;
    private boolean hasMore;
    private List<StoreListElement> stores;

    public static StoreListDTO from(
            final List<Store> stores,
            final List<Like> likes,
            final int page,
            final int size,
            final boolean hasMore
    ) {
        StoreListDTO result =  StoreListDTO.builder()
                .totalCount(stores.size())
                .page(page)
                .size(size)
                .hasMore(hasMore)
                .stores(stores.stream().map(StoreListElement::from).toList())
                .build();
        result.updateLikeable(likes);
        return result;
    }

    private void updateLikeable(
            final List<Like> likes
    ) {
        for (Like like : likes) {
            for (StoreListElement storeListElement : stores) {
                if (storeListElement.getStoreId()
                        .equals(like.getStore().getId())){
                    storeListElement.updateLikeable(false);
                }
            }
        }
    }

    @Data
    private static class StoreListElement{
        private Long storeId;
        private String name;
        private String address;
        private String imagePath;
        private int likeCount;
        private int reviewCount;
        private Long usableDonation;
        private double longitude;
        private double latitude;
        private boolean likeable;

        public static StoreListElement from(
                final Store store
        ) {
            return StoreListElement.builder()
                    .storeId(store.getId())
                    .name(store.getName())
                    .address(store.getAddress())
                    .imagePath(store.getImagePath())
                    .likeCount(store.getLikeCount())
                    .usableDonation(store.getUsableDonation())
                    .longitude(store.getLongitude())
                    .latitude(store.getLatitude())
                    .build();
        }

        public void updateLikeable(boolean likeable){
            this.likeable = likeable;
        }

        @Builder
        public StoreListElement(
                final Long storeId,
                final String name,
                final String address,
                final String imagePath,
                final int likeCount,
                final int reviewCount,
                final Long usableDonation,
                final double longitude,
                final double latitude
        ) {
            this.storeId = storeId;
            this.name = name;
            this.address = address;
            this.imagePath = imagePath;
            this.likeCount = likeCount;
            this.reviewCount = reviewCount;
            this.usableDonation = usableDonation;
            this.longitude = longitude;
            this.latitude = latitude;
            this.likeable = true;
        }
    }
}
