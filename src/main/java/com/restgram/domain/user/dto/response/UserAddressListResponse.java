package com.restgram.domain.user.dto.response;

import com.restgram.domain.address.dto.response.AddressResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record UserAddressListResponse(

    Integer range,
    // 초기 유저 설정 지역
    AddressResponse emdAddress,
    AddressResponse siggAddress,
    AddressResponse sidoAddress,
    List<AddressResponse> emdAddressList,
    List<AddressResponse> siggAddressList,
    List<AddressResponse> sidoAddressList
    
) {

}
