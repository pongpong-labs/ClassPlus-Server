package pnu.classplus.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pnu.classplus.domain.entity.Role;

@ApiModel(description="회원 정보")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDto {
    @ApiModelProperty(dataType="String", required=true, example="park0691")
    private String id;
    @ApiModelProperty(dataType="String", required=true, example="1q2w3e4r")
    private String password;
    @ApiModelProperty(value="회원 유형", dataType="String", required=true, example="ROLE_STUDENT")
    private Role role;
    @ApiModelProperty(dataType="String", required=true, example="홍길동")
    private String name;
    @ApiModelProperty(dataType="String", required=true, example="test@pusan.ac.kr")
    private String email;
    @ApiModelProperty(dataType="String", required=true, example="부산시 금정구 장전동")
    private String address;
    @ApiModelProperty(dataType="String", required=true, example="010-1234-5678")
    private String phone;
    @ApiModelProperty(value="대학교", dataType="String", required=true, example="부산대학교")
    private String univ;
    @ApiModelProperty(value="학과", dataType="String", required=true, example="정보컴퓨터공학부")
    private String dept;
}