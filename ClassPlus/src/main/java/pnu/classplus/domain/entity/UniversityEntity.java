package pnu.classplus.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "UNIVERSITY")
public class UniversityEntity {
    @Id
    @Column(name = "UNIV_IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 40, nullable = false)
    private String name;

    @OneToMany(mappedBy = "university", fetch = FetchType.EAGER)
    private Set<DepartmentEntity> deptList = new HashSet<DepartmentEntity>();

    @ToString.Exclude
    @OneToOne(mappedBy = "university")
    private MemberEntity member;
}
