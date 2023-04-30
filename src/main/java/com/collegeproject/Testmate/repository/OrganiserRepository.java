package com.collegeproject.Testmate.repository;

import com.collegeproject.Testmate.entity.Organiser;
import net.bytebuddy.description.type.TypeDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganiserRepository extends JpaRepository<Organiser, Long> {

	@Query("SELECT u FROM Organiser u WHERE u.email = ?1")
	Organiser findByEmail(String email);

	@Modifying
	@Query("UPDATE Organiser u SET u.image=?1 WHERE u.id=?2")
	void updateImage(byte[] image,Long id);

	@Modifying
	@Query("UPDATE Organiser u set u.lname=?1,u.address=?2,u.country=?3,u.email=?4,u.lang=?5,u.organisation=?6," +
			"u.state=?7,u.mobNo=?8,u.pincode=?9 WHERE u.id=?10")
	void updateById(String lname,String address,String country,String email,String lang,String org,String State,String mobNo,String pincode,Long id);



}
