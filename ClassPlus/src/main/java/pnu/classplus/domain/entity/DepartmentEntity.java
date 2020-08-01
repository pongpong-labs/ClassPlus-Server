package pnu.classplus.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "univ_idx", nullable = false)
    private UniversityEntity university;

    @ToString.Exclude
    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    private List<LectureEntity> lecList = new ArrayList<LectureEntity>();
}
