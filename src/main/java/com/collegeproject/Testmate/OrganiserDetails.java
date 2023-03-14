package com.collegeproject.Testmate;

import com.collegeproject.Testmate.entity.Organiser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class OrganiserDetails implements UserDetails {

	public OrganiserDetails(Organiser org ) {
		this.org = org;
	}

	public Organiser getOrg() {
		return org;
	}

	private Organiser org;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}


	@Override
	public String getPassword() {
		return org.getPassword();
	}

	@Override
	public String getUsername() {
		return org.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
