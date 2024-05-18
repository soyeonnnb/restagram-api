package com.restgram.domain.user.dto.response;

import com.restgram.domain.address.dto.res.AddressRes;
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
    List<AddressRes> emdAddressList;
    List<AddressRes> siggAddressList;
    List<AddressRes> sidoAddressList;
}
