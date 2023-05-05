package com.collegeproject.Testmate.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor()
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(nullable=false,unique=true,length=50)
	@NonNull  private String email;
	
	@Column(nullable=false,length=50)
	@NonNull  private String name;





}
