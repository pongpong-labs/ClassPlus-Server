package pnu.classplus.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "DEPARTMENT")
public class DepartmentEntity {
    @Id
    @Column(name = "DEPT_IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 50, nullable = false)
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "univ_idx", nullable = false)
    private UniversityEntity university;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    @OneToMany(mappedBy = "department")
    private Set<LectureEntity> lecSet = new HashSet<LectureEntity>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    @OneToOne(mappedBy = "department")
    private MemberEntity member;
}
