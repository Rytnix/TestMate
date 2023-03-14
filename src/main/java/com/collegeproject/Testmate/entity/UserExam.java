package com.collegeproject.Testmate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_exams")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserExam {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "status",nullable = false)
    private int status;
    @ManyToOne(targetEntity = Exam.class)
    @JoinColumn(name= "exam_id", nullable=false)
    private Exam exams;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name= "user_id", nullable=false)
    private User user;

}
