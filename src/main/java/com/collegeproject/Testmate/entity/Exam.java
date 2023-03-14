package com.collegeproject.Testmate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false,length=50,name = "title")
	private String title;
	
	@Column(length=100 ,name = "description")
	private String description;
	
	@Column(length=150,name = "instructions")
	private String	instructions;

	@DateTimeFormat(pattern = "dd/MM/yyyy h:mm a")
	@Column(nullable = false,name = "start_date")
	private Date startDate;
	
	@Column(nullable=false,length=5,name = "marks")
	private int marksOfEachQuestion ;
	
	@Column(nullable=false,length=5,name = "time")
	private int examTime;
	
	@Column(length=5,name = "negative_marks",nullable = true)
	private int negativeMarkOfEachQuestion;
	
	

	@ManyToOne
	@JoinColumn(name= "organiser_id", nullable= false)
	private Organiser organisers;

	@OneToMany(mappedBy="exams")
	private List<Question> questions= new ArrayList<Question>();

	public List<UserExam> getUserExam() {
		return userExam;
	}

	public void setUserExam(List<UserExam> userExam) {
		this.userExam = userExam;
	}

	@OneToMany(mappedBy="exams")
	private List<UserExam> userExam;


	public List<Question> getQuestions() {
		return questions;
	}

	public String getExamCode(){
		String[] titleList=this.title.split(" ");
		StringBuilder Code=new StringBuilder();
		for (String t:titleList) {
			char c=t.charAt(0);
			if(Character.isLetter(c) || Character.isDigit(c)){
				Code.append(c);
			}
		}
		String[] letters={"EXAM","XAM","MOK","LT","MDS","FPA","KU","POU","KIH","RTU"};
		if(Code.toString().equals("")){
			int index= (int) (this.id % 10);
			Code.append(letters[index]);
		}
		return Code.toString().toUpperCase()+"-"+this.id.toString();
	}

	public int getNextQuestionNo(Long question_id) {
		List<Question> questions = this.getQuestions();
		int i;
		for (i=0;i<questions.size();i++){
			if(questions.get(i).getId()==question_id){
				break;
			}
		}
		if(i==questions.size()-1){
			return 1;
			//Last Question
		}else{
			return i+2;
		}
	}
	public boolean isLastQuestion(Long question_id){
		List<Question> questions_list = this.getQuestions();
		return questions_list.get(questions_list.size() - 1).getId().equals(question_id);
	}

	public int calculateScore(int correctAnswers,int incorrectAnswer){

		int totalMarks=this.getMarksOfEachQuestion()*this.getQuestions().size();
		int markScored=correctAnswers*this.getMarksOfEachQuestion();
		int negativeMark=incorrectAnswer*this.getNegativeMarkOfEachQuestion();
		int actualScore=markScored-negativeMark;

		return actualScore;
	}

	public boolean isOver(){
		long time=this.getExamTime()*60*1000;
		long currentTime=new Date().getTime();
		long examTime =this.getStartDate().getTime();
		return examTime+time <= currentTime;
	}
	public boolean isStarted(){
		long time=this.getExamTime()*60*1000;
		long currentTime=new Date().getTime();
		long examTime =this.getStartDate().getTime();
		if(examTime < currentTime && currentTime < examTime + time)
			return true;
		else
			return false;
	}

	public String getExamStatus(){
		long time=this.getExamTime()*60*1000;
		long currentTime=new Date().getTime();
		long examTime =this.getStartDate().getTime();
		if(isOver()){
			return "Exam Over";
		} else if(examTime < currentTime && currentTime < examTime + time){
			return "Started";
		}else{
			return "Not Started";
		}
	}
	public  long getRemainingTime(){
		long currentTime=new Date().getTime();
		long examTime =this.getStartDate().getTime();
		long time=this.getExamTime()*60*1000;
		return examTime+time-currentTime;
	}

	public long getStartTimeInMilliseconds(){
		return this.getStartDate().getTime()-new Date().getTime();
	}
}
