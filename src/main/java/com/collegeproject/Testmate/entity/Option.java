package com.collegeproject.Testmate.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "options")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Option {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name= "queston_id", nullable=false)
	@NonNull private Question questions;

	@Column(length=50,name = "statement")
	@NonNull private String option;


}
