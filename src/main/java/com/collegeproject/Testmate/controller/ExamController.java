package com.collegeproject.Testmate.controller;


import com.collegeproject.Testmate.OrganiserDetails;
import com.collegeproject.Testmate.config.Email;
import com.collegeproject.Testmate.entity.Exam;
import com.collegeproject.Testmate.entity.Organiser;
import com.collegeproject.Testmate.entity.UserExam;
import com.collegeproject.Testmate.repository.ExamRepository;
import com.collegeproject.Testmate.repository.OrganiserRepository;
import com.collegeproject.Testmate.repository.UserAnswerRepository;
import com.collegeproject.Testmate.repository.UserExamRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class    ExamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamController.class);

    @Resource
    private JavaMailSender javaMailSender;

     @Autowired
    Configuration fmconfiguration;
    @Autowired
    ExamRepository repo;
    @Autowired
    OrganiserRepository organiserRepository;

    @Autowired
    UserAnswerRepository userAnswerRepository;

    @Autowired
    UserExamRepository userExamRepository;

    @GetMapping("/organiser/exams")
    public String showExams(Model model){
        Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof OrganiserDetails){
            Organiser org = ((OrganiserDetails) user).getOrg();
            model.addAttribute("exams",repo.findByOrganiserId(org.getId()));
            model.addAttribute("id",org.getId());
            model.addAttribute("name",org.getName());
            return "organiser/exam/list-exam";
        }
        else {
            return OrganiserController.LOGIN_ROUTE;
        }
    }

    @GetMapping("/organiser/exams/create")
    public String showCreateExam(Model model){
        Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof OrganiserDetails) {
            Organiser org = ((OrganiserDetails) user).getOrg();
            model.addAttribute("exam", new Exam());
            model.addAttribute("id", org.getId());
            model.addAttribute("name", org.getName());
            return "organiser/exam/create-exam";
        }

        return OrganiserController.LOGIN_ROUTE;
    }

    @PostMapping("/organiser/exams/create")
    public String createExam(Exam exam){
        Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof OrganiserDetails){
            exam.setOrganisers(((OrganiserDetails) user).getOrg());
            System.out.println("I am is post exam"+ exam.getInstructions());
            repo.save(exam);
            return "redirect:/organiser/exams";            
        }
        else{
            return OrganiserController.LOGIN_ROUTE;

        }
    }
    @GetMapping("/organiser/exams/edit")
    public String editExam(@RequestParam(name = "id")Long exam_id, Model model){
        Exam oldExam=repo.findById(exam_id).get();
        model.addAttribute("oldExam",oldExam);
        Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Organiser org = ((OrganiserDetails) user).getOrg();
        model.addAttribute("id",org.getId());
        model.addAttribute("name",org.getName());
        return "organiser/exam/edit";
    }

    @PostMapping("/organiser/exams/edit")
    public String editSaveExam(Exam exam){
        System.out.println("hellooooooo");
        Exam exam1=repo.findById(exam.getId()).get();
        exam.setOrganisers(exam1.getOrganisers());
        repo.save(exam);
        return "redirect:/organiser/exams";
    }

    @GetMapping("/organiser/exams/view")
    public String viewExam(@RequestParam(name = "id",required = true ) Long id,Model model){
        Exam exam=repo.findById(id).get();
        model.addAttribute("exam",exam);
        Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Organiser org = ((OrganiserDetails) user).getOrg();
        model.addAttribute("id",org.getId());
        model.addAttribute("name",org.getName());
        return "organiser/exam/view-exam";
    }

    @GetMapping("/organiser/exams/result")
    public String viewResult(@RequestParam(name = "id",required = true ) Long id,Model model){
        Exam exam=repo.findById(id).get();
        model.addAttribute("exam",exam);
        List<UserExam> examUsers=exam.getUserExam();

        int presentCount=userExamRepository.findPresentUsersCount(exam.getId());
        int adbsetCount=userExamRepository.findAbsentUsersCount(exam.getId());

        model.addAttribute("presentCount",presentCount);
        model.addAttribute("adbsetCount",adbsetCount);

        HashMap<Long,Integer> correctAnswers=new HashMap<>();
        HashMap<Long,Integer> incorrectAnswers=new HashMap<>();
        HashMap<Long,Integer> score=new HashMap<>();
        for (UserExam userExam:examUsers) {
            int correct=userAnswerRepository.findCorrectAnswersCount(userExam.getId());
            int incorrect=userAnswerRepository.findInCorrectAnswersCount(userExam.getId());
            correctAnswers.put(userExam.getId(),correct);
            incorrectAnswers.put(userExam.getId(),incorrect);
            score.put(userExam.getId(),exam.calculateScore(correct,incorrect));
        }

        Object user= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       if(user instanceof OrganiserDetails){
			Organiser org = ((OrganiserDetails) user).getOrg();
			model.addAttribute("id",org.getId());
			model.addAttribute("name",org.getName());
			Optional<Organiser> orgI = organiserRepository.findById(org.getId());
			model.addAttribute("O",orgI.get());
		}

        model.addAttribute("examUsers",examUsers);
        model.addAttribute("correctAnswers",correctAnswers);
        model.addAttribute("incorrectAnswers",incorrectAnswers);
        model.addAttribute("score",score);

        return "organiser/result/list33";
    }

    @GetMapping("/organiser/exam/mail")
    public String sendEmail(@RequestParam(name = "id",required = true ) Long id, RedirectAttributes redirectAttributes) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        Exam exam=repo.findById(id).get();
        List<UserExam> examUsers=exam.getUserExam();
        for (UserExam userExam:examUsers) {

            SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy, hh:mm:ss a");
            String strDate = formatter.format(exam.getStartDate());
            String user_name = userExam.getUser().getName();
            String user_email = userExam.getUser().getEmail();
            String user_pass = userExam.getPassword();
            String exam_code = exam.getExamCode();
            String exam_duration = exam.getExamTime()+" Minutes";
            String exam_title = exam.getTitle();

            Email email = new Email();
            Map<String,Object> map = new HashMap<>();
            map.put("user_name",user_name);
            map.put("user_email",user_email);
            map.put("user_pass",user_pass);
            map.put("exam_code",exam_code);
            map.put("strDate",strDate);
            map.put("exam_duration",exam_duration);
            map.put("exam_title",exam_title);
            email.setModel(map);
            helper.setTo(user_email);
            helper.setSubject("Login Credentials for "+ exam_title +" Examination");
            helper.setText(geContentFromTemplate(email.getModel()),true);
            helper.addInline("logo1.png",new ClassPathResource("templates/images/image-1.png"));
            helper.addInline("logo2.png",new ClassPathResource("templates/images/image-2.png"));
            helper.addInline("logo3.png",new ClassPathResource("templates/images/image-3.png"));
            helper.addInline("logo4.png",new ClassPathResource("templates/images/image-4.png"));
            helper.addInline("logo5.png",new ClassPathResource("templates/images/image-5.png"));
           try {
               javaMailSender.send(msg);

            } catch (MailException e) {
               e.printStackTrace();
            }
            System.out.println("Mail Sent to : "+user_email);
        }
        redirectAttributes.addFlashAttribute("success_message","Mail Sent Successfully");
        return "redirect:/organiser/exams/view?id="+id;
    }
    public String geContentFromTemplate(Map< String, Object > model)     {
        StringBuffer content = new StringBuffer();
       String html="";
        try {
            Template t = fmconfiguration.getTemplate("email-template.ftl");
            html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
//            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmconfiguration.getTemplate("email-template.ftl"),model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }
}
