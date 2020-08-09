package pnu.classplus.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "UNIVERSITY")
public class UniversityEntity {
    @Id
    @Column(name = "UNIV_IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 40, nullable = false)
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "university", fetch = FetchType.EAGER)
    private Set<DepartmentEntity> deptSet = new HashSet<DepartmentEntity>();

    @ToString.Exclude
    @OneToOne(mappedBy = "university")
    private MemberEntity member;

}
