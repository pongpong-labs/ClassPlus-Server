package pnu.classplus.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lecture_script")
public class LectureScriptEntity {
    @Id
    @Column(name="LEC_SCRIPT_IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "LEC_REG_IDX", nullable = false)
    private LectureRegisteredEntity lectureRegistered;

    @Column(nullable = false)
    private int week;

    @Column(nullable = false)
    private int num_per_week;

    @Column(length = 400, nullable = false)
    private String summary;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String script;

    @Column(length = 5000, nullable = false)
    private String feedback;
}
