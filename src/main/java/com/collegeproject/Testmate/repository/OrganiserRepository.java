package com.collegeproject.Testmate.repository;

import com.collegeproject.Testmate.entity.Organiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganiserRepository extends JpaRepository<Organiser, Long> {

	@Query("SELECT u FROM Organiser u WHERE u.email = ?1")
	Organiser findByEmail(String email);
	
}
