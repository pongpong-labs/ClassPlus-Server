package pnu.classplus.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lecture_registered")
public class LectureRegisteredEntity {
    @Id
    @Column(name = "LEC_REG_IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "MEMBER_IDX", nullable = false)
    private MemberEntity student;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "LEC_DETAILS_IDX", nullable = false)
    private LectureDetailsEntity lectureDetails;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "lectureRegistered", fetch = FetchType.LAZY)
    private Set<LectureScriptEntity> lectureScriptSet = new HashSet<LectureScriptEntity>();

    @Column(nullable = false)
    private int take_count;

    @Column(nullable = false)
    private boolean enabled;
}
