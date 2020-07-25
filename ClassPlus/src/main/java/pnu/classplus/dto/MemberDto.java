package pnu.classplus.dto;

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

    private String id;
    private String password;
    private Role role;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String univ;
    private String dept;
}