package pnu.classplus.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pnu.classplus.domain.entity.MemberEntity;
import pnu.classplus.domain.entity.Role;

@Getter
@NoArgsConstructor
@ToString
public class MemberDto {
    private String id;
    private String password;
    private int role;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String univ;
    private String dept;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .id(id)
                .password(password)
                .name(name)
                .email(email)
                .address(address)
                .phone(phone)
                .univ(univ)
                .dept(dept)
                .enabled(true)
                .build();
    }
}