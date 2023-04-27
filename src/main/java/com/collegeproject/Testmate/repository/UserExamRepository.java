package com.collegeproject.Testmate.repository;

import com.collegeproject.Testmate.entity.UserExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserExamRepository extends JpaRepository<UserExam, Long> {

    @Query("select u from UserExam u where u.exams.id = ?1 and u.user.id = ?2")
    UserExam findUserExamByUser(Long exam_id,Long user_id);

    @Query("select count(u) from UserExam u where u.exams.id = ?1 and (u.status = 1 or u.status = 2)")
    int findPresentUsersCount(Long exam_id);

    @Query("select count(u) from UserExam u where u.exams.id = ?1 and u.status = 0")
    int findAbsentUsersCount(Long exam_id);
}
