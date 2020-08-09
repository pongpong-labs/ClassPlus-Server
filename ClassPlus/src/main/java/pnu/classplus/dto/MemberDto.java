package pnu.classplus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pnu.classplus.domain.entity.Role;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDto {
    private Long idx;
    private String uid;
    private String password;
    private Role role;
    private String name;
    private String email;
    private Long univ;
    private Long dept;
}