package com.collegeproject.Testmate.repository;

import com.collegeproject.Testmate.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    @Query("SELECT u FROM UserAnswer u WHERE u.userexam.id = ?1 AND u.questions.id=?2")
    UserAnswer findByUserQuestion(Long user_id,Long question_id);

    @Query("SELECT count(u) FROM UserAnswer u WHERE u.userexam.id = ?1 AND u.answer_status=1")
    int findCorrectAnswersCount(Long useExamId);

    @Query("SELECT count(u) FROM UserAnswer u WHERE u.userexam.id = ?1 AND u.answer_status=0")
    int findInCorrectAnswersCount(Long useExamId);

}
