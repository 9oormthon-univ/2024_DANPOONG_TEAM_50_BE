package com.example.mymoo.domain.store.service.Impl;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.account.exception.AccountException;
import com.example.mymoo.domain.account.exception.AccountExceptionDetails;
import com.example.mymoo.domain.account.repository.AccountRepository;
import com.example.mymoo.domain.store.dto.response.MenuListDTO;
import com.example.mymoo.domain.store.dto.response.StoreDetailDTO;
import com.example.mymoo.domain.store.dto.response.StoreListDTO;
import com.example.mymoo.domain.store.entity.Like;
import com.example.mymoo.domain.store.entity.Menu;
import com.example.mymoo.domain.store.entity.Store;
import com.example.mymoo.domain.store.exception.StoreException;
import com.example.mymoo.domain.store.exception.StoreExceptionDetails;
import com.example.mymoo.domain.store.repository.LikeRepository;
import com.example.mymoo.domain.store.repository.StoreRepository;
import com.example.mymoo.domain.store.service.StoreService;
import com.example.mymoo.domain.store.util.StoreUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;

@Service @Transactional
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final LikeRepository likeRepository;
    private final AccountRepository accountRepository;

    private final Logger log = LoggerFactory.getLogger(getClass());

    // 위치기반으로 음식점을 조회
    public StoreListDTO getAllStoresByLocation(
            final Double logt,
            final Double lat,
            final int page,
            final int size,
            final Long accountId
    ){
        return null;
    }

    //keyword 를 포함하는 음식점명, 주소를 가진 음식점을 조회
    public StoreListDTO getAllStoresByKeyword(
            final String keyword,
            final Pageable pageable,
            final Long accountId
    ){
        return null;
    }

    //모든 음식점을 조회
    public StoreListDTO getAllStores(
            final Pageable pageable,
            final Long accountId
    ){
        return null;
    }

    //음식점 id로 음식점을 조회
    public StoreDetailDTO getStoreById(
            final Long storeId,
            final Long accountId
    ){
        return null;
    }

    //음식점 id로 메뉴를 조회
    public MenuListDTO getMenusByStoreId(
            final Long id
    ){
        return null;
    }

    //음식점 좋아요 수정
    public String updateStoreLikeCount(
            final Long storeId,
            final Long accountId
    ){
        return null;
    }

}
