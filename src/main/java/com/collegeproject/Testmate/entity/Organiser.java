package com.collegeproject.Testmate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organisers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organiser implements Serializable {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Column(nullable=false,unique=true,length=50)
	private String email;
	
	@Column(nullable=false,length=50)
	private String name;
	
	@Column(nullable=false,length=250)
	private String password;
	@OneToMany(mappedBy="organisers")
	private List<Exam> exams= new ArrayList<Exam>();


}