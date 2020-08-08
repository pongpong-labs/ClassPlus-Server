package pnu.classplus.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "university", fetch = FetchType.EAGER)
    private Set<DepartmentEntity> deptSet = new HashSet<DepartmentEntity>();

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "university")
    private MemberEntity member;

    @Override
    public boolean equals(Object obj) {
        UniversityEntity other;
        if (obj instanceof UniversityEntity) {
            other = (UniversityEntity) obj;
            return (idx.equals(other.idx)) && (name.equals(other.name));
        } else {
            return false;
        }
    }
}
