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
}
