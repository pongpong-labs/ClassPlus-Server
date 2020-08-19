package pnu.classplus.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "MEMBER")
public class MemberEntity extends BaseTimeEntity implements UserDetails {

    @Id
    @Column(name="MEMBER_IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 20, updatable = false, nullable = false)
    private String uid;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne
    @JoinColumn(name = "UNIV_IDX")
    private UniversityEntity university;

    @OneToOne
    @JoinColumn(name = "DEPT_IDX")
    private DepartmentEntity department;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "professor", fetch = FetchType.EAGER)
    private Set<LectureDetailsEntity> lectureSet = new HashSet<LectureDetailsEntity>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private Set<LectureRegisteredEntity> lectureRegisteredSet = new HashSet<LectureRegisteredEntity>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private Set<MessageEntity> messageSenderSet = new HashSet<MessageEntity>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private Set<MessageEntity> messageReceiverSet = new HashSet<MessageEntity>();

    @Column(nullable = false)
    private boolean enabled;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return uid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}