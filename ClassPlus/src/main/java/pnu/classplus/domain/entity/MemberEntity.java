package pnu.classplus.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "MEMBER")
public class MemberEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 20, nullable = false)
    private String id;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Role role = Role.ROLE_ADMIN;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 300, nullable = false)
    private String address;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(length = 20, nullable = false)
    private String univ;

    @Column(length = 20, nullable = false)
    private String dept;

    @Column(nullable = false)
    private boolean enabled;

    @Builder
    public MemberEntity(String id, String password, String name, String email, String address, String phone,
                        String univ, String dept, boolean enabled) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.univ = univ;
        this.dept = dept;
        this.enabled = enabled;
    }
}