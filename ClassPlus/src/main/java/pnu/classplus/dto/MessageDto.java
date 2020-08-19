package pnu.classplus.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MessageDto {
    private Long receiver_mem_idx;
    private String message;
}
