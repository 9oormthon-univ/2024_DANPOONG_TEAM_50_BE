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
        List<Store> foundStores = storeRepository.findAll();
        Map<Integer, Store> storeMap = new HashMap<>();
        for(Store store : foundStores){
            storeMap.put(StoreUtil.calculateDistance(logt, lat, store.getLongitude(), store.getLatitude()), store);
        }
        List<Integer> storeList = new ArrayList<>(storeMap.keySet());
        storeList.sort(Comparator.naturalOrder());
        List<Store> selectedStores = new ArrayList<>();
        for (int i=page*size ; i<page*size+size ;i++) {
            selectedStores.add(storeMap.get(storeList.get(i)));
        }
        List<Like> likes = likeRepository.findAllByAccount_Id(accountId);
        return StoreListDTO.from(selectedStores, likes, page, size, false);
    }

    //keyword 를 포함하는 음식점명, 주소를 가진 음식점을 조회
    public StoreListDTO getAllStoresByKeyword(
            final String keyword,
            final Pageable pageable,
            final Long accountId
    ){
        Slice<Store> storesFindByKeyword = storeRepository.findAllByNameContainsOrAddressContains(keyword, keyword, pageable);
        List<Store> selectedStores = storesFindByKeyword.stream().toList();
        List<Like> likes = likeRepository.findAllByAccount_Id(accountId);
        return StoreListDTO.from(selectedStores, likes, pageable.getPageNumber(), pageable.getPageSize(), storesFindByKeyword.hasNext());
    }

    //모든 음식점을 조회
    public StoreListDTO getAllStores(
            final Pageable pageable,
            final Long accountId
    ){
        Slice<Store> stores = storeRepository.findAll(pageable);
        List<Like> likes = likeRepository.findAllByAccount_Id(accountId);
        return StoreListDTO.from(stores.getContent(), likes, stores.getNumber(), stores.getSize(), stores.hasNext());
    }

    //음식점 id로 음식점을 조회
    public StoreDetailDTO getStoreById(
            final Long storeId,
            final Long accountId
    ){
        Store found = storeRepository.findById(storeId).orElseThrow(() -> new StoreException(StoreExceptionDetails.STORE_NOT_FOUND));
        Optional<Like> foundLike = likeRepository.findByAccount_IdAndStore_Id(accountId,storeId);
        return StoreDetailDTO.from(found, foundLike.isEmpty());
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
