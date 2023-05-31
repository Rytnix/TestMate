package com.collegeproject.Testmate.controller;


import com.collegeproject.Testmate.Utils.CSVHelper;
import com.collegeproject.Testmate.Utils.RandomString;
import com.collegeproject.Testmate.Utils.ResponseMessage;
import com.collegeproject.Testmate.entity.*;
import com.collegeproject.Testmate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class UserController {


    @Autowired
    UserRepository userRepo;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    UserExamRepository userExamRepository;

    @Autowired
    UserAnswerRepository userAnswerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    OptionRepository optionRepository;

    @PostMapping("{examCode}/login")
    public String loginUser(@PathVariable(name = "examCode")String examCode,
                            @RequestParam(name = "email")String email,
                            @RequestParam(name = "password")String password,
                            Model model,
                            HttpSession session){
        String redirectUrl="redirect:/"+examCode+"/login";
        try{
            if(checkValidExamCode(examCode)){
                User user=userRepo.findByEmail(email);
                if(user==null){
                    redirectUrl+="?error=1";
                    throw new Exception();
                }
                Long exam_id= Long.valueOf(examCode.split("-")[1]);
                Exam exam=examRepository.findById(exam_id).get();
                Long currentTime=new Date().getTime();
                Long examTime = exam.getStartDate().getTime();
                if(exam.isOver()){
                    //Exam is over Sorry you are late
                    redirectUrl+="?error=6";
                    throw new Exception();
                }
                if(examTime-currentTime>900000){
                    // You can login only before 15 mins of exam start time
                    redirectUrl+="?error=4";
                    throw new Exception();
                }
                if(currentTime-examTime>1800000){
                    // You can login only after 30 mins of exam start time
                    redirectUrl+="?error=5";
                    throw new Exception();
                }

                UserExam userExam=userExamRepository.findUserExamByUser(exam_id,user.getId());
                if(userExam==null){
                    // Invalid Email or password
                    redirectUrl+="?error=1";
                    throw new Exception();
                }else if(userExam.getPassword().equals(password)){

                    if(userExam.getStatus()==2){
                        //Exam Already Submitted
                        redirectUrl+="?error=3";
                        throw new Exception();
                    }
                    //check If exam Started or not

                    session.setAttribute("user_exam_id",userExam.getId());
                    session.setAttribute("exam_id",exam_id);
                    userExam.setStatus(1);// Mark Present for that user
                    userExamRepository.save(userExam);
                    return "redirect:/"+examCode+"/instruction";

                }else{
                    redirectUrl+="?error=1";
                    throw new Exception();
                }
            }else{
                throw new Exception();
            }
        }catch(Exception e){
            return redirectUrl;
        }
    }

    public void saveCSVFile(MultipartFile file, Long exam_id) {
        try {
            List<User> users = CSVHelper.csvToUsers(file.getInputStream());
            for (User u: users) {
                Exam exam=examRepository.findById(exam_id).get();
                User user=userRepo.findByEmail(u.getEmail());
                if(user==null){
                    user=new User(u.getEmail(), u.getName());
                }else{
                    user.setName(u.getName());
                }
                userRepo.save(user);
                String password= RandomString.getAlphaNumericString(8);
                UserExam userExam=new UserExam(password,exam,user);
                userExamRepository.save(userExam);
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    @PostMapping("/user/csv/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(name = "exam_id")Long exam_id, RedirectAttributes redirectAttributes) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                saveCSVFile(file,exam_id);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                redirectAttributes.addFlashAttribute("success_message", message);
                ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                redirectAttributes.addFlashAttribute("error_message", message);
                ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        } else {
            message = "Please upload a csv file!";
            redirectAttributes.addFlashAttribute("error_message", message);
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        return "redirect:/organiser/exams/view?id="+exam_id;
    }

    @PostMapping("/organiser/user/add")
    public String addUser(@RequestParam(name = "exam_id")Long exam_id,@RequestParam(name="name")String name,@RequestParam(name="email")String email){
        Exam exam=examRepository.findById(exam_id).get();
        User user=userRepo.findByEmail(email);
        if(user==null){
            user=new User(email,name);
        }else{
            user.setName(name);
        }
        userRepo.save(user);
        String password= RandomString.getAlphaNumericString(8);
        UserExam userExam=new UserExam(password,exam,user);
        userExamRepository.save(userExam);

        return "redirect:/organiser/exams/view?id="+exam_id;
    }

    @GetMapping("organsier/user/delete")
    public String deleteUser(@RequestParam(name = "id")Long userExamId){
        UserExam userExam=userExamRepository.findById(userExamId).get();
        Long exam_id=userExam.getExams().getId();
        userExamRepository.delete(userExam);
        return "redirect:/organiser/exams/view?id="+exam_id;
    }

    public boolean checkValidExamCode(String examCode){
        Long exam_id= Long.valueOf(examCode.split("-")[1]);
        if(examRepository.findById(exam_id).isPresent()){
            Exam exam=examRepository.findById(exam_id).get();
            return exam.getExamCode().equals(examCode);
        }else{
            return false;
        }
    }



    @GetMapping("{examCode}/login")
    public String showLogin(HttpSession session, @PathVariable(name = "examCode")String examCode, Model model){
        try{
            if(checkValidExamCode(examCode)){
                Long exam_id= Long.valueOf(examCode.split("-")[1]);
                Exam exam=examRepository.findById(exam_id).get();
                model.addAttribute("exam",exam);
                return "user/login";
            }else{
                throw new Exception();
            }
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("user/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("{examcode}/instruction")
    public String showInstructionPage(Model model,@PathVariable(name = "examcode")String examCode,HttpSession session){
        String redirectUrl="redirect:/"+examCode+"/login";
        try{
            if(checkValidExamCode(examCode)){
                if(isLoggedInForExam(session,examCode)){
                    Long user_id= (Long) session.getAttribute("user_exam_id");
                    long exam_id= Long.parseLong(examCode.split("-")[1]);
                    Exam exam=examRepository.findById(exam_id).get();

                    UserExam userExam=userExamRepository.findById(user_id).get();
                    if(userExam.getStatus()==2){
                        //Exam Already Submitted
                        redirectUrl+="?error=3";
                        throw new Exception();
                    }

                    if(exam.isOver()){
                        //Exam Over
                        redirectUrl="redirect:/"+examCode+"/final";
                        throw new Exception();
                    }

                    model.addAttribute("exam",exam);
                    return "user/instruction";
                }else{
                    throw new Exception();
                }
            }else{
                throw new Exception();
            }
        }catch (Exception e){
            return redirectUrl;
        }
    }

    private boolean isLoggedInForExam(HttpSession session, String examCode) {

        long exam_id= Long.parseLong(examCode.split("-")[1]);
        Long session_user_id= (Long) session.getAttribute("user_exam_id");
        Long session_exam_id= (Long) session.getAttribute("exam_id");

        //Check User is logged in For current Exam Code
        if(exam_id != session_exam_id)
            return false;

        //Check User Authorization for Current Exam
        if(userExamRepository.findById(session_user_id).isEmpty())
            return false;

        return true;
    }

    @GetMapping(value = {"/{examCode}/exam", "/{examCode}/exam/{question_no}"})
    public String showUserDashboard(HttpSession session, @PathVariable(name = "examCode")String examCode, Model model,@PathVariable(name = "question_no",required = false)Integer question_no){
        System.out.println("Hello form exam");
        String redirectUrl="redirect:/"+examCode+"/login";
        try{
            if(checkValidExamCode(examCode)){
                if(isLoggedInForExam(session,examCode)){
                    Long user_id= (Long) session.getAttribute("user_exam_id");
                    long exam_id= Long.parseLong(examCode.split("-")[1]);

                    UserExam userExam=userExamRepository.findById(user_id).get();
                    if(userExam.getStatus()==2){
                        //Exam Already Submitted
                        redirectUrl+="?error=3";
                        throw new Exception();
                    }
                    Exam exam=examRepository.findById(exam_id).get();

                    if(exam.isOver()){
                        //Exam Over
                        redirectUrl="redirect:/"+examCode+"/final";
                        throw new Exception();
                    }
                    if(!exam.isStarted()){
                        //Exam Not started yet
                        redirectUrl="redirect:/"+examCode+"/instruction";
                        throw new Exception();
                    }

                    List<Question> all_questions = exam.getQuestions();

                    if(question_no==null)
                        question_no=1;

                    Question currentQuestion = all_questions.get(question_no - 1);

                    //Getting Answers
                    HashMap<Long, Long> answers=new HashMap<>();
                    for (Question question:all_questions) {
                        UserAnswer userAnswer=userAnswerRepository.findByUserQuestion(user_id,question.getId());
                        if(userAnswer!=null){
                            answers.put(question.getId(),userAnswer.getAnswer().getId());
                        }
                    }

                    model.addAttribute("answers",answers);
                    model.addAttribute("question",currentQuestion);
                    model.addAttribute("exam",exam);
                    model.addAttribute("question_no",question_no);
                    return "user/dashboard";

                }else{
                    throw new Exception();
                }
            }else{
                throw new Exception();
            }
        }
        catch (Exception e){
            return redirectUrl;
        }
    }

    @PostMapping("{examcode}/submit")
    public String saveAnswers(HttpSession session,@PathVariable(name = "examcode")String examcode,@RequestParam(name = "answer_id",required = false)Long answer_id,@RequestParam(name = "question_id")Long question_id){

        String redirectUrl="redirect:/"+examcode+"/exam";
        try{
            if(checkValidExamCode(examcode)){
                if(isLoggedInForExam(session,examcode)){

                    Long user_id= (Long) session.getAttribute("user_exam_id");
                    Long exam_id= Long.parseLong(examcode.split("-")[1]);
                    Exam exam = examRepository.findById(exam_id).get();

                    if(exam.isOver()){
                        throw new Exception();
                    }

                    UserAnswer userAnswer = userAnswerRepository.findByUserQuestion(user_id, question_id);
                    if(answer_id==null){
                        if(userAnswer!=null)
                            userAnswerRepository.delete(userAnswer);
                    }else{
                        if(userAnswer==null){
                            userAnswer = new UserAnswer();
                        }
                        UserExam userExam=userExamRepository.findById(user_id).get();
                        Question question=questionRepository.findById(question_id).get();
                        Option answer = optionRepository.findById(answer_id).get();
                        boolean correct=false;
                        if(question.getAnswer().getAnswer().getId().equals(answer.getId())){
                            correct=true;
                        }
                        userAnswer.setAnswer(answer);
                        userAnswer.setUser(userExam);
                        userAnswer.setQuestions(question);
                        userAnswer.setAnswerStatus(correct);
                        userAnswerRepository.save(userAnswer);
                    }
                    return "redirect:/"+examcode+"/exam/"+exam.getNextQuestionNo(question_id);
                }else{
                    throw new Exception();
                }
            }else{
                throw new Exception();
            }
        }catch (Exception e){
            return redirectUrl;
        }
    }

    @GetMapping("{examcode}/final")
    public String submitExam(@PathVariable(name = "examcode")String examCode,HttpSession session){
        if(checkValidExamCode(examCode)){
            if(isLoggedInForExam(session,examCode)){

                Long user_id= (Long) session.getAttribute("user_exam_id");

                UserExam userExam=userExamRepository.findById(user_id).get();
                userExam.setStatus(2);
                userExamRepository.save(userExam);
                session.setAttribute("result",true);

                return "redirect:/"+examCode+"/result";
            }
            else{
                return "/";
            }
        }else{
            return "/";
        }
    }

    @GetMapping("/{examcode}/result")
    public String showResult(@PathVariable(name = "examcode")String examCode, HttpSession session, Model model)
    {
      try{
          if(checkValidExamCode(examCode)){
              if(isLoggedInForExam(session,examCode)){
                  Boolean result= (Boolean) session.getAttribute("result");
                  Long user_id= (Long) session.getAttribute("user_exam_id");
                  Long exam_id= (Long) session.getAttribute("exam_id");
                  UserExam userExam=userExamRepository.findById(user_id).get();
                  if(result==true){
                      Exam exam=examRepository.findById(exam_id).get();

                      int correct=userAnswerRepository.findCorrectAnswersCount(user_id);
                      int incorrect=userAnswerRepository.findInCorrectAnswersCount(user_id);
                      int score=exam.calculateScore(correct,incorrect);

                      model.addAttribute("exam",exam);
                      model.addAttribute("correctAnswers",correct);
                      model.addAttribute("incorrectAnswers",incorrect);
                      model.addAttribute("score",score);

                      return "user/result";
                  }
              }
          }
      }catch(Exception e){
          return "redirect:/";
      }
      return "redirect:/";
    }

    @PostMapping("goto/exam")
    public  String goToExam(@RequestParam(name = "examcode")String examcode){
        return"redirect:/"+examcode+ "/login";
    }
}
