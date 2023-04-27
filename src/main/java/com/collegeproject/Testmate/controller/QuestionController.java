package com.collegeproject.Testmate.controller;

import com.collegeproject.Testmate.entity.Answer;
import com.collegeproject.Testmate.entity.Exam;
import com.collegeproject.Testmate.entity.Option;
import com.collegeproject.Testmate.entity.Question;
import com.collegeproject.Testmate.repository.AnswerRepository;
import com.collegeproject.Testmate.repository.ExamRepository;
import com.collegeproject.Testmate.repository.OptionRepository;
import com.collegeproject.Testmate.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
@Controller
public class QuestionController {

    @Autowired
    ExamRepository examRepo;

    @Autowired
    QuestionRepository repo;

    @Autowired
    OptionRepository optionRepo;

    @Autowired
    AnswerRepository answerRepo;

    @PostMapping("/organiser/question/add")
    public String addQuestion(@RequestParam(name = "question")String question, @RequestParam(name = "option[]") List<String> options, @RequestParam(name="exam_id")Long exam_id,@RequestParam(name = "answer")Integer answer){
        Exam exam=examRepo.findById(exam_id).get();
        Question q=new Question();
        q.setStatement(question);
        q.setExams(exam);
        repo.save(q);

        ArrayList<Option> optionList= new ArrayList<>();
        for (String option : options) {
            Option o=new Option(q, option);
            optionList.add(o);
            optionRepo.save(o);
        }

        Answer a=new Answer(q,optionList.get(answer-1));
        answerRepo.save(a);

        return "redirect:/organiser/exams/view?id="+exam_id;
    }
    @PostMapping("/organiser/question/edit")
    public String editQuestion(@RequestParam(name = "question")String question, @RequestParam(name = "option[]") List<String> options, @RequestParam(name="question_id")Long question_id,@RequestParam(name = "answer")Integer answer){

        Question ques=repo.findById(question_id).get();
        ques.setStatement(question);
        repo.save(ques);

        List<Option> ops = ques.getOptions();
        ops.get(0).setOption(options.get(0));
        optionRepo.save(ops.get(0));
        ops.get(1).setOption(options.get(1));
        optionRepo.save(ops.get(1));
        ops.get(2).setOption(options.get(2));
        optionRepo.save(ops.get(2));
        ops.get(3).setOption(options.get(3));
        optionRepo.save(ops.get(3));

        Answer ans = ques.getAnswer();
        ans.setAnswer(ops.get(answer-1));
        answerRepo.save(ans);

        return "redirect:/organiser/exams/view?id="+ques.getExams().getId();
    }

    @GetMapping("/organiser/question/delete")
    public String deleteQuestion(@RequestParam(name = "question_id") Long question_id,@RequestParam(name="exam_id")Long exam_id){
        Question q=repo.findById(question_id).get();
        Answer answer = answerRepo.findById(q.getAnswer().getId()).get();
        answerRepo.delete(answer);
        optionRepo.deleteAll(q.getOptions());
        repo.delete(q);
        return "redirect:/organiser/exams/view?id="+exam_id;
    }

}
