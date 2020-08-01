package pnu.classplus.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString(exclude = "department")
@Entity
@Table(name = "LECTURE")
public class LectureEntity {
    @Id
    @Column(name = "LEC_IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 30, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "dept_idx", nullable = false)
    private DepartmentEntity department;
}