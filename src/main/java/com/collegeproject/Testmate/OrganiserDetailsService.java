package com.collegeproject.Testmate;

import com.collegeproject.Testmate.entity.Organiser;
import com.collegeproject.Testmate.repository.OrganiserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.io.IOException;

public class OrganiserDetailsService implements UserDetailsService {

	@Resource
	OrganiserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Organiser org=repo.findByEmail(email);
		if(org==null) {
			throw new UsernameNotFoundException("User not found");
		}

		return new OrganiserDetails(org);
	}
}
