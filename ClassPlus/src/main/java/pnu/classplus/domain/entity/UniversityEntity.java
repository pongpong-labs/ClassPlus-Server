package pnu.classplus.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<DepartmentEntity> deptList = new ArrayList<DepartmentEntity>();
}
