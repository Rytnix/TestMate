package com.collegeproject.Testmate.controller;


import com.collegeproject.Testmate.OrganiserDetails;
import com.collegeproject.Testmate.entity.Organiser;
import com.collegeproject.Testmate.repository.ExamRepository;
import com.collegeproject.Testmate.repository.OrganiserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrganiserController {

	@Autowired
	OrganiserRepository repo;

	@Autowired
	ExamRepository examRepository;

	public static String LOGIN_ROUTE="redirect:/organiser/login";

	@GetMapping("organiser/register")
	public String register(Model model) {
		model.addAttribute("organiser",new Organiser());
		return "organiser/register";
	}

	@GetMapping("organiser/forgotpass")
	public String fogotpass(Model model) {
		model.addAttribute("organiser",new Organiser());
		return "organiser/forgotpass";
	}
	@PostMapping("organiser/register")
	public String registerPost(Organiser org) {
		System.out.println("hello");
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		String encodedPassword=encoder.encode(org.getPassword());
		org.setPassword(encodedPassword);
		repo.save(org);
		return "redirect:/organiser/dashboard";
	}

	@GetMapping("organiser/login")
	public String login() {

		Object user= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user instanceof OrganiserDetails){
			System.out.println("Hello");
			return "redirect:/organiser/dashboard";
		}
		return "organiser/login";
	}

	@GetMapping("organiser/dashboard")
	public String dashboard(Model model) {
		int examcount=0;
		Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user instanceof OrganiserDetails){
			Organiser org = ((OrganiserDetails) user).getOrg();
			examcount = examRepository.findByOrganiserId(org.getId()).size();
		}
		model.addAttribute("examcount",examcount);
		return "organiser/dashboard";
	}
}
