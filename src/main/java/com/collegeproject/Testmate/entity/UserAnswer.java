package com.collegeproject.Testmate.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_answer")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name= "queston_id", nullable=false)
    private Question questions;

    @OneToOne
    @JoinColumn(name= "option_id", nullable=false)
    private Option answer;

    @OneToOne
    @JoinColumn(name= "user_id", nullable=false)
    private UserExam userexam;

    @Column(name = "answer_status" ,nullable = false)
    private Boolean answer_status;

    public Question getQuestions() {
        return questions;
    }

    public void setQuestions(Question questions) {
        this.questions = questions;
    }

    public Option getAnswer() {
        return answer;
    }

    public void setAnswer(Option answer) {
        this.answer = answer;
    }

    public UserExam getUser() {
        return userexam;
    }

    public void setAnswerStatus(boolean answerStatus){
        this.answer_status=answerStatus;
    }

    public void setUser(UserExam userexam) {
        this.userexam = userexam;
    }
}
