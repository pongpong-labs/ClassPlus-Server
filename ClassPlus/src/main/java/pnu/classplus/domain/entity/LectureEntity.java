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
@Table(name = "LECTURE")
public class LectureEntity {
    @Id
    @Column(name = "LEC_IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 30, nullable = false)
    private String name;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "dept_idx", nullable = false)
    private DepartmentEntity department;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    @OneToMany(mappedBy = "lecture", fetch = FetchType.EAGER)
    private Set<LectureDetailsEntity> lectureSet = new HashSet<LectureDetailsEntity>();
}