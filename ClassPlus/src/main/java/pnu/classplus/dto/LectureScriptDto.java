package pnu.classplus.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LectureScriptDto {
    private long lec_details_idx;
    private int week;
    private int num_per_week;
    private String summary;
    private String script;
    private int speed_score;
    private int accuracy_score;
    private String feedback;
}
