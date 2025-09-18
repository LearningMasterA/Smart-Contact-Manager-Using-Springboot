package com.smart.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {

    private final DaoAuthenticationProvider authenticationProvider;
	
	@Autowired
	private UserRepository repo;
	

	@Value("${file.uploadDir}")
    private String uploadDir;


    UserController(DaoAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
	
	
//	Method to add ommon data
	@ModelAttribute
	public void addCommonData(Model m,Principal principal) {
		String username=principal.getName();
		System.out.println(username);
		User user = this.repo.getUserByUserName(username);
		System.out.println(user);
		m.addAttribute("user", user);
	}
	
//	Dashboard Home
	@RequestMapping("/index")
	public String dashboard(Model m,Principal principal) {
//		String username=principal.getName();
//		System.out.println(username);
//		User user = this.repo.getUserByUserName(username);
//		System.out.println(user);
//		m.addAttribute("user", user);
		m.addAttribute("title", "User Dashboard ");
		return "normal/user_dashboard";
	}
	
	@RequestMapping("test")
	public String home() {
		return "normal/test";
	}
	
	
//	Open add contact form handler
	
	@RequestMapping("/add_contact")
	public String openAddContactForm(Model m) {
		m.addAttribute("title", "User");
		m.addAttribute("contact",new Contact());
		
		return "normal/add_contact";
	}
	
	@PostMapping("/process_contact")
	public String processContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file, Principal principal){
		try {
		String name=principal.getName();
		User user = this.repo.getUserByUserName(name);
		contact.setUser(user);
		
		user.getContacts().add(contact);
		this.repo.save(user);
		
		System.out.println("Contact saved : "+contact);
		System.out.println("Contact added successfully...");
		}catch(Exception e) {
			System.out.println("ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		return "normal/add_contact";
	} 

}
