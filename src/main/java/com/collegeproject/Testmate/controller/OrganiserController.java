package com.collegeproject.Testmate.controller;


import com.collegeproject.Testmate.OrganiserDetails;
import com.collegeproject.Testmate.config.ImageUtils;
import com.collegeproject.Testmate.entity.Organiser;
import com.collegeproject.Testmate.repository.ExamRepository;
import com.collegeproject.Testmate.repository.OrganiserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


@Controller
public class OrganiserController {

	@Autowired
	OrganiserRepository repo;

	@Autowired
	ExamRepository examRepository;

	public static String LOGIN_ROUTE="redirect:/organiser/login";
	public static String DASHBOARD_ROUTE="redirect:/organiser/account-settings";

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
	@GetMapping(value = "organiser/account-settings")
	public String accountSettings(Model model) {
		model.addAttribute("organiser",new Organiser());
		Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user instanceof OrganiserDetails){
			Organiser org = ((OrganiserDetails) user).getOrg();
			model.addAttribute("id",org.getId());
			System.out.println("id at get acc "+ org.getId());
			Optional<Organiser> orgI = repo.findById(org.getId());
			System.out.println("lname "+orgI.get().getLname());
			model.addAttribute("O",orgI.get());
		}
		return "organiser/account-settings";
	}

	@GetMapping("organiser/notifications")
	public String notification(Model model) {

		model.addAttribute("organiser",new Organiser());
		Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user instanceof OrganiserDetails){
			Organiser org = ((OrganiserDetails) user).getOrg();
			model.addAttribute("id",org.getId());
			model.addAttribute("name",org.getName());
			Optional<Organiser> orgI = repo.findById(org.getId());
			model.addAttribute("O",orgI.get());

		}

		return "organiser/notifications";
	}
	@PostMapping(value = "organiser/account-settings/updateDetails")
	@Transactional
	public String updateOrganiser(Organiser organiser,Model model) {
		System.out.println("hello");
		Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user instanceof OrganiserDetails){
			Organiser org = ((OrganiserDetails) user).getOrg();
			System.out.println("state "+organiser.getState());
		    repo.updateById(organiser.getLname(),organiser.getAddress(),organiser.getCountry(),organiser.getEmail(),organiser.getLang(),organiser.getOrganisation(),
					        organiser.getState(),organiser.getMobNo(),organiser.getPincode(),org.getId());
			System.out.println("id at get post "+ org.getId());
		}
		return DASHBOARD_ROUTE;
	}
	@GetMapping(value = "organiser/account-settings/deactivate")
	public String deactivateId(Long id) {
	     repo.deleteById(id);
		return LOGIN_ROUTE;
	}
	@GetMapping(value = "organiser/um")
	public String underMaintainance() {
		return "organiser/um";
	}
	@RequestMapping("/error-404")
	public String handle404Error() {
		return "organiser/um";
	}
	@GetMapping("/image/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
		byte[] imageBytes = ImageUtils.decompressImage(repo.findById(id).get().getImage());
		String contentType = "image/jpeg";
		if (Arrays.equals(Arrays.copyOfRange(imageBytes, 0, 4), new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47})) {
			contentType = "image/png";
		} else if (Arrays.equals(Arrays.copyOfRange(imageBytes, 0, 3), new byte[]{(byte) 0x47, (byte) 0x49, (byte) 0x46})) {
			contentType = "image/gif";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(contentType));
		return new ResponseEntity<byte[]>(imageBytes, headers, HttpStatus.OK);
	}

	@PostMapping("organiser/account-settings/imageUpload")
	@Transactional
   public String imageUpload(@RequestParam MultipartFile img) throws IOException {

		Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user instanceof OrganiserDetails){
			Organiser org = ((OrganiserDetails) user).getOrg();
//			 repo.findById(org.getId()).get().getName();
			repo.updateImage(ImageUtils.compressImage(img.getBytes()),org.getId());
		}
		System.out.println("save Successfull");
		return DASHBOARD_ROUTE;
   }

	@PostMapping("organiser/register")
	public String registerPost(Organiser org) {
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
		int activecount=0;
		String name="";
		Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user instanceof OrganiserDetails){
			Organiser org = ((OrganiserDetails) user).getOrg();
			examcount = examRepository.findByOrganiserId(org.getId()).size();
			name =  repo.findById(org.getId()).get().getName();
			model.addAttribute("id",org.getId());
		}
		model.addAttribute("examcount",examcount);
		model.addAttribute("activecount",activecount);
		model.addAttribute("name",name);
		return "organiser/dashboard";
	}
}
