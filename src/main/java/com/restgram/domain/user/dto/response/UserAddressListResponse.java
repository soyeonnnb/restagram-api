package com.restgram.domain.user.dto.response;

import com.restgram.domain.address.dto.res.AddressRes;
import com.restgram.domain.address.dto.res.EmdAddressRes;
import jakarta.annotation.Nullable;
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
    List<AddressRes> addressList;
    List<AddressRes> emdAddressList;
    List<AddressRes> siggAddressList;
    List<AddressRes> sidoAddressList;
}
