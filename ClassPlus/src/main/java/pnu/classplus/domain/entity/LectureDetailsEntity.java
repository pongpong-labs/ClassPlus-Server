package pnu.classplus.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lecture_details")
public class LectureDetailsEntity {
    @Id
    @Column(name="LEC_DETAILS_IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private int section;

    @Column(nullable = false)
    private int week;

    @Column(nullable = false)
    private int count_per_week;

    @Column(nullable = false)
    private LocalDateTime start_time;

    @Column(nullable = false)
    private LocalDateTime end_time;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "PROFESSOR_MEM_IDX", nullable = false)
    private MemberEntity professor;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "LEC_IDX", nullable = false)
    private LectureEntity lecture;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "lectureDetails", fetch = FetchType.EAGER)
    private Set<LectureRegisteredEntity> lectureRegisteredSet = new HashSet<LectureRegisteredEntity>();
}