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
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    private final DaoAuthenticationProvider authenticationProvider;
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	public ContactRepository contactRepo;
	

//	@Value("${file.uploadDir}")
//    private String uploadDir;


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
	public String processContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file, Principal principal,RedirectAttributes redirectAttributes){
		try {
		String name=principal.getName();
		User user = this.repo.getUserByUserName(name);
		
		
//		Processjng and uploading file
		if(file.isEmpty()) {
			System.out.println("file is empty");
		}
		
		else {
//			upload the file to folder and then save it in contacts
			String filename=file.getOriginalFilename();
//			String filename = new ClassPathResource("static/img").getFilename();
//			Path path=Paths.get(filename.getAbsolutePath()+file.separator+filename.getOriginalName());
			Path uploadDir=Paths.get("src/main/resources/static/img");
			Path path=uploadDir.resolve(filename);
			Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
			
			contact.setImage(filename);
			System.out.println("Image is uploaded");
		}
		
		contact.setUser(user);
		
		user.getContacts().add(contact);
		this.repo.save(user);
		
		System.out.println("Contact saved : "+contact);
		System.out.println("Contact added successfully...");
		
		
//		Message success
		redirectAttributes.addFlashAttribute("message",
                new Message("Successfully added !!", "alert-success"));
		
		
		}catch(Exception e) {
			System.out.println("ERROR:" + e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message",
                    new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
		} 
		return "redirect:/user/add_contact";
	} 
	
	
	
//	per page 5 contacts
//	curr page =0
	
	@GetMapping("/show_contacts/{page}")
	public String showContacts(@PathVariable("page")Integer page, Model m,Principal p) {
		m.addAttribute("title", "Show User Contacts");
		
//		Contacts ki list ko bhejna hai
//		String userName=principal.getName();
//		User name = this.repo.getUserByUserName(userName);
//		List<Contact> contacts=name.getContacts();
		
		String name = p.getName();
		User userName = this.repo.getUserByUserName(name);
		int id = userName.getId();
		Pageable pageable = PageRequest.of(page, 5);
		
		Page<Contact> list = this.contactRepo.findContactsByUser(id,pageable);
		m.addAttribute("contacts", list);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", list.getTotalPages());
		System.out.println(list);
		
		
		
		return "normal/show_contacts";
	}
	
	
	
//	per page 5 contacts
//	curr page =0

}
