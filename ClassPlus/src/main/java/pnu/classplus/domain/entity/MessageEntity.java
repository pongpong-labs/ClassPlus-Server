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
@Table(name = "message")
public class MessageEntity extends BaseTimeEntity {
    @Id
    @Column(name="MSG_IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="SENDER_MEM_IDX", nullable = false)
    private MemberEntity sender;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="RECEIVER_MEM_IDX", nullable = false)
    private MemberEntity receiver;

    @Column(length=5000, nullable=false)
    private String message;
}
