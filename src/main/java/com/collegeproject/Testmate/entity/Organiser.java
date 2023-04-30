package com.collegeproject.Testmate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import javax.persistence.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organisers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organiser implements Serializable {

	private static final long serialVersionUID = -1333441338884572822L;
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Column(nullable=false,unique=true,length=50)
	private String email;
	
	@Column(nullable=false,length=50)
	private String name;

	@Column(length=50)
	private String lname;

	@Column(length=50)
	private String organisation;

	@Column(length=50)
	private String mobNo;

	@Column(length=50)
	private String address;

	@Column(length=50)
	private String pincode;

	@Column(length=50)
	private String state;

	@Column(length=50)
	private String country;
//
	@Column(length=50)
	private String lang;

	@Column(nullable = false,length=250)
	private String password;

	private byte[] image;

	private Boolean isActive;

	@OneToMany(mappedBy="organisers")
	private List<Exam> exams= new ArrayList<Exam>();

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		oos.writeObject(image);
	}
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		image = (byte[]) ois.readObject();
	}
}