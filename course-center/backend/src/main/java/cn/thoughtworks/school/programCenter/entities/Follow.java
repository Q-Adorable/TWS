package cn.thoughtworks.school.programCenter.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "follow")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long programId;
    private Long tutorId;
    private Long studentId;
}
