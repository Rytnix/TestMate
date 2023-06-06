package com.collegeproject.Testmate.controller;

import com.collegeproject.Testmate.OrganiserDetails;
import com.collegeproject.Testmate.entity.Organiser;
import com.collegeproject.Testmate.repository.OrganiserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class AppController {

	@Autowired
	OrganiserRepository repo;
	@GetMapping({"", "/", "/index", "/home"})
	public String index() {
		return "testmate-homepage/index";
	}

	@GetMapping({"/about.html", "/about"})
	public String about() {
		return "testmate-homepage/about";
	}

	@GetMapping("/contact-us.html")
	public String contactUs() {
		return "contact";
	}

	@GetMapping("/load-card-body")
	public String loadCardBody(Model model) {
		// Populate the necessary data for the card body (question_no, question, answers, etc.) in the model

		return "/user/card-body-fragment"; // Return the Thymeleaf fragment name
	}

	@GetMapping("/organiser/chatwindow")
	public String loadChatWindow(Model model) {
		// Populate the necessary data for the card body (question_no, question, answers, etc.) in the model


		model.addAttribute("organiser",new Organiser());
		Object user= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user instanceof OrganiserDetails){
			Organiser org = ((OrganiserDetails) user).getOrg();
			model.addAttribute("id",org.getId());
			model.addAttribute("name",org.getName());
			Optional<Organiser> orgI = repo.findById(org.getId());
			model.addAttribute("O",orgI.get());
		}
		return "/organiser/chat"; // Return the Thymeleaf fragment name
	}

	@GetMapping("/organiser/a")
	public String loadChart(Model model) {
		// Populate the necessary data for the card body (question_no, question, answers, etc.) in the model

		return "/organiser/a"; // Return the Thymeleaf fragment name
	}

	@GetMapping("/organiser/final")
	public String loadChartdata(Model model) {
		// Populate the necessary data for the card body (question_no, question, answers, etc.) in the model

		return "/organiser/final"; // Return the Thymeleaf fragment name

	}
	@GetMapping("/student/gotoexam")
	public String loadExamPage(Model model) {
		// Populate the necessary data for the card body (question_no, question, answers, etc.) in the model
		return "user/gotoExam"; // Return the Thymeleaf fragment name


	}
	}