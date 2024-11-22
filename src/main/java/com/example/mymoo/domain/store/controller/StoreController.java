package com.example.mymoo.domain.store.controller;

import com.example.mymoo.domain.store.dto.response.MenuListDTO;
import com.example.mymoo.domain.store.dto.response.StoreDetailDTO;
import com.example.mymoo.domain.store.dto.response.StoreListDTO;
import com.example.mymoo.domain.store.dto.response.StoreResponseDTO;
import com.example.mymoo.domain.store.exception.StoreException;
import com.example.mymoo.domain.store.exception.StoreExceptionDetails;
import com.example.mymoo.domain.store.repository.StoreRepository;
import com.example.mymoo.domain.store.service.StoreService;
import com.example.mymoo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final StoreRepository storeRepository;

    @GetMapping("")
    @Operation(
            summary = "[공통]음식점 전체 조회",
            description = "가게를 검색하여 조회하는 api 입니다. logt(경도), lat(위도) 를 보내면 현재 위치기반, keyword 를 보내면 keyword 기반검색, 아무것도 보내지 않으면 모든 상점을 조회합니다. *likeable 은 좋아요를 누를 수 있는지 없는지 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
            }
    )
    public ResponseEntity<StoreListDTO> getAllStore(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "page 의 순서를 의미합니다.") @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "page 의 크기를 의미합니다.") @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @Parameter(description = "정렬 기준입니다. 오름차순: asc 내림차순: desc") @RequestParam(value = "sort", required = false, defaultValue = "asc") String sort,
            @Parameter(description = "정렬할 대상입니다. 가게명: name, 주소: address") @RequestParam(value = "sortby", required = false, defaultValue = "name") String sortby,
            @Parameter(description = "검색할 keyword를 의미합니다.") @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "현재위치의 경도를 의미합니다.") @RequestParam(value = "logt", required = false) Double logt,
            @Parameter(description = "현재위치의 위도를 의미합니다.")  @RequestParam(value = "lat", required = false) Double lat
    ){
        Long accountId = userDetails.getAccountId();
        System.out.println(accountId);
        if (logt != null && lat != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(storeService.getAllStoresByLocation(logt, lat, page, size, accountId));
        }else if(keyword != null){
            if (sort.equals("desc")){
                Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortby);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(storeService.getAllStoresByKeyword(keyword, pageable, accountId));
            }else if(sort.equals("asc")){
                Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortby);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(storeService.getAllStoresByKeyword(keyword, pageable, accountId));
            }else{
                throw new StoreException(StoreExceptionDetails.QUERY_PARAMETER_INVALID);
            }
        }else{
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortby);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(storeService.getAllStores(pageable, accountId));
        }
    }

    @GetMapping("{storeId}")
    @Operation(
            summary = "[공통]음식점 상세 조회",
            description = "id 값으로 기준으로 특정 가게를 조회하는 api 입니다. *likeable 은 좋아요를 누를 수 있는지 없는지 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
            }
    )
    public ResponseEntity<StoreDetailDTO> getStoreById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "가게의 id값 입니다.") @PathVariable("storeId") Long storeId
    ){
        Long accountId = userDetails.getAccountId();
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStoreById(storeId, accountId));
    }

    @GetMapping("{storeId}/menus")
    @Operation(
            summary = "[공통]가게 메뉴 조회",
            description = "id 값으로 기준으로 특정 가게의 메뉴 조회하는 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
            }
    )
    public ResponseEntity<MenuListDTO> getMenusByStoreId(
            @Parameter(description = "가게의 id값 입니다.") @PathVariable("storeId") Long id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getMenusByStoreId(id));
    }

    @PatchMapping("{storeId}")
    @Operation(
            summary = "[공통]가게 좋아요 증가/감소",
            description = "id 값으로 기준으로 특정 가게의 좋아요를 반영합니다. 이미 누른 경우 좋아요가 1 감소되고 누르지 않은 경우 좋아요가 1 증가됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
            }
    )
    public ResponseEntity<StoreResponseDTO> updateStoreLikeCount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "가게의 id값 입니다.") @PathVariable("storeId") Long id
    ){
        Long accountId = userDetails.getAccountId();
        String result = storeService.updateStoreLikeCount(id, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new StoreResponseDTO(HttpStatus.NO_CONTENT, storeRepository.findById(id).get(), result)
        );
    }

}
