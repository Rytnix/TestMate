package com.collegeproject.Testmate.repository;

import com.collegeproject.Testmate.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT e FROM Exam e WHERE e.organisers.id = ?1")
    List<Exam> findByOrganiserId(Long organiser_id);
//    @Query("SELECT e FROM Exam e WHERE e.organisers.id = ?2 and e.ex")
//    List<Exam> findActiveExam(Long organiser_id);
}
