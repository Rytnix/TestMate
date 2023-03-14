package com.collegeproject.Testmate.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
public class Question {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false,length=300)
	private String statement;
	@ManyToOne
	@JoinColumn(name= "exam_id", nullable=false)
	private Exam exams;
	@OneToMany(mappedBy="questions")
	private List<Option> options= new ArrayList<Option>();
	@OneToOne(mappedBy="questions")
	private Answer answer;
	public String getBaseEncodedId(){
		return Base64.getEncoder().encode(new byte[]{this.id.byteValue()}).toString();
	}

}
