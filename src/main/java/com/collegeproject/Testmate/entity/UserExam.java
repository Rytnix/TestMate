package com.collegeproject.Testmate.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_exams")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UserExam {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    @NonNull
    private String password;

    @Column(name = "status",nullable = false)
    private int status;
    @ManyToOne(targetEntity = Exam.class)
    @JoinColumn(name= "exam_id", nullable=false)
    @NonNull
    private Exam exams;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name= "user_id", nullable=false)
    @NonNull
    private User user;

}
