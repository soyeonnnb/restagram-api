package com.restgram.domain.address.controller;

import com.restgram.domain.address.dto.response.AddressResponse;
import com.restgram.domain.address.service.AddressService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/address")
@Slf4j
public class AddressController {

    private final AddressService addressService;

    // 시도 리스트 가져오기
    @GetMapping("/sido")
    public CommonResponse getSidoList() {
        List<AddressResponse> addressResponseList = addressService.getSidoList();
        return CommonResponse.builder()
                .data(addressResponseList)
                .success(true)
                .code(HttpStatus.OK.value())
                .build();
    }

    // 시군구 리스트 가져오기 -> 시도 ID에 해당하는
    @GetMapping("/sigg")
    public CommonResponse getSiggList(@RequestParam("sido-id") Long sidoId) {
        List<AddressResponse> addressResponseList = addressService.getSiggList(sidoId);
        return CommonResponse.builder()
                .data(addressResponseList)
                .success(true)
                .code(HttpStatus.OK.value())
                .build();
    }

    // 읍면동 리스트 가져오기 -> 읍면동 ID에 해당하는
    @GetMapping("/emd")
    public CommonResponse getSidoList(@RequestParam("sigg-id") Long siggId) {
        List<AddressResponse> addressResponseList = addressService.getEmdList(siggId);
        return CommonResponse.builder()
                .data(addressResponseList)
                .success(true)
                .code(HttpStatus.OK.value())
                .build();
    }

}
