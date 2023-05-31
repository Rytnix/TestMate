package com.collegeproject.Testmate.config;

import com.collegeproject.Testmate.OrganiserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource datasource;
	@Bean
	public UserDetailsService userDetailsService() {

		return new OrganiserDetailsService();
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()

				.antMatchers("/organiser/register","/organiser/forgotpass","/v2/api-docs",
						"/configuration/ui",
						"/swagger-resources/**",
						"/configuration/security",
						"/swagger-ui.html",
						"/webjars/**","/organiser/chatwindow").permitAll()
				.antMatchers("/organiser/**").authenticated()
				.and()
				.formLogin()
					.loginPage("/organiser/login")
					.usernameParameter("email")
					.passwordParameter("password")
					.loginProcessingUrl("/organiser/login")
					.defaultSuccessUrl("/organiser/dashboard")
					.permitAll()
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/organiser/logout")).logoutSuccessUrl("/organiser/login?logout")
				.permitAll();

	}



}
