package com.example.mymoo.domain.child.dto.controller;


import com.example.mymoo.domain.child.dto.request.ChildReqeustDTO;
import com.example.mymoo.domain.child.dto.response.ChildResponseDTO;
import com.example.mymoo.domain.child.entity.Child;
import com.example.mymoo.domain.child.service.ChildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/children")
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;

    @Operation(
        summary = "[공통] 아동 추가 회원가입 및 후원자 계정(기본)에서 아동 계정으로 권한 변경",
        description = "account 가 아동회원일 경우 아동에 대한 정보를 추가하는 api 입니다. " +
                "account signUp 후에 accountId 값과 cardNumber 를 requestBody 에 보내주시고 " +
                "cardNumber 는 16자리 숫자 보내주시면 됩니다.",
        responses = {
                @ApiResponse(responseCode = "201", description = "아동 추가 회원가입 성공"),
        }
    )
    @PostMapping("/signup")
    public ResponseEntity<ChildResponseDTO> createChild(
        @Valid @RequestBody ChildReqeustDTO request
    ) {
        Child newChild = childService.createChild(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ChildResponseDTO(HttpStatus.CREATED, newChild, "tb_child row++"));
    }
}
