package com.collegeproject.Testmate.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name= "queston_id", nullable=false)
    @NonNull private Question questions;

    @OneToOne
    @JoinColumn(name= "option_id", nullable=false)
    @NonNull private Option option;
    public void setAnswer(Option answer) {
        this.option = answer;
    }

    public Option getAnswer() {
        return option;
    }
}
