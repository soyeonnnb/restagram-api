package com.restgram.domain.user.dto.response;

import com.restgram.domain.address.dto.response.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressListResponse {
    Integer range;
    List<AddressResponse> addressList;
    List<AddressResponse> emdAddressList;
    List<AddressResponse> siggAddressList;
    List<AddressResponse> sidoAddressList;
}
