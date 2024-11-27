package com.example.mymoo.domain.store.service;

import com.example.mymoo.domain.store.dto.response.MenuListDTO;
import com.example.mymoo.domain.store.dto.response.StoreDetailDTO;
import com.example.mymoo.domain.store.dto.response.StoreListDTO;
import org.springframework.data.domain.Pageable;


public interface StoreService {

    StoreListDTO getAllStoresByLocation(
            final Double logt,
            final Double lat,
            final int page,
            final int size,
            final Long accountId
    );
    StoreListDTO getAllStoresByKeyword(
            final String keyword,
            final Pageable pageable,
            final Long accountId,
            final Double logt,
            final Double lat
    );
    StoreDetailDTO getStoreById(
            final Long storeid,
            final Long accountId
    );
    MenuListDTO getMenusByStoreId(
            final Long id
    );
    String updateStoreLikeCount(
            final Long storeId,
            final Long accountId
    );
    String updateStoreBookMark(
            final Long storeId,
            final Long accountId
    );
    StoreListDTO getAllStoresBookMarked(
            final Long accountId
    );

}
