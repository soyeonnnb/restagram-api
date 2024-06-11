package com.restgram.domain.user.dto.response;

import com.restgram.domain.address.dto.response.AddressResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressListResponse {

  private Integer range;
  // 초기 유저 설정 지역
  private AddressResponse emdAddress;
  private AddressResponse siggAddress;
  private AddressResponse sidoAddress;
  private List<AddressResponse> emdAddressList;
  private List<AddressResponse> siggAddressList;
  private List<AddressResponse> sidoAddressList;
}
