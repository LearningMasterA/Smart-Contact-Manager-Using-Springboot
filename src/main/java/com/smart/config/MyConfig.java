package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//@EnableWebSecurity
//public class MyConfig{
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//		http.authorizeHttpRequests(null)
//	}
//
//}

@EnableWebSecurity
@Configuration
public class MyConfig {
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	            .authenticationProvider(authenticationProvider())
	            .build();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/admin/**").hasRole("ADMIN")
	            .requestMatchers("/user/**").hasRole("USER")
	            .requestMatchers("/**").permitAll()
	        )
	        .formLogin(form -> form
	        	    .loginPage("/signin")               // your custom login page
	        	    .loginProcessingUrl("/dologin")     // form action URL
	        	    .defaultSuccessUrl("/user/index", true) // redirect after login
	        	    .permitAll()
	        	)

	        .csrf(csrf -> csrf.disable());

	    return http.build();
	}

	
	/*
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception{ auth.authenticationProvider(authenticationProvider()); }
	 * 
	 * @Override protected void configure(HttpSecurity http) throws Exception{
	 * http.authorizeHttpRequests().antMatchers("/admin/**").hasRole("ADMIN")
	 * .antMatchers("/user/**").hasRole("USER")
	 * .antMatchers("/**").permitAll().formLogin().and().csrf().disable(); }
	 */
}
