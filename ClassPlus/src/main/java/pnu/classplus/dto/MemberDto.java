package pnu.classplus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pnu.classplus.domain.entity.Role;

@Schema(description="회원 정보")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDto {

    @Schema(type="Long", required=true, example="691")
    private Long idx;

    @Schema(type="String", required=true, example="park0691")
    private String uid;

    @Schema(type="String", required=true, example="1q2w3e4r")
    private String password;

    @Schema(description="회원 유형", type="String", required=true, example="ROLE_STUDENT")
    private Role role;

    @Schema(type="String", required=true, example="홍길동")
    private String name;

    @Schema(type="String", required=true, example="test@pusan.ac.kr")
    private String email;

    @Schema(description="대학교", type="String", required=true, example="부산대학교")
    private Long univ;

    @Schema(description="학과", type="String", required=true, example="정보컴퓨터공학부")
    private Long dept;
}