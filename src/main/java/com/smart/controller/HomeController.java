package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.SmartContactManagerApplication;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    private final SmartContactManagerApplication smartContactManagerApplication; 
	@Autowired
	private UserRepository repo;

    HomeController(SmartContactManagerApplication smartContactManagerApplication) {
        this.smartContactManagerApplication = smartContactManagerApplication;
    }
	
	public UserRepository getRepo() {
		return repo;
	}

	public void setRepo(UserRepository repo) {
		this.repo = repo;
	}
    @GetMapping("/")
	public String home(Model m) {
    	m.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
    
    
    
    @GetMapping("/about")
   	public String about(Model m) {
       	m.addAttribute("title","About - Smart Contact Manager");
   		return "about";
   	}
    
    
    @GetMapping("/signup")
   	public String signUp(Model m) {
       	m.addAttribute("title","About - Smart Contact Manager");
       	m.addAttribute("user", new User());
   		return "signup";
   	}
    
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user,@RequestParam(value="agreement",defaultValue = "false") boolean agreement,Model m,RedirectAttributes redirectAttributes,BindingResult result) {
    	try {
    		if(!agreement) {
        		System.out.println("You have not agreed to the terms and conditions");
        		throw new Exception("You have not agreed to the terms and conditions");
        	}
    		
    		if(result.hasErrors()) {
    			System.out.println("ERROR "+result.toString());
    			m.addAttribute("user", user);
    			return "redirect:/signup";
    		}
        	
        	user.setRole("ROLE_USER");
        	user.setEnabled(true);
        	user.setImageUrl("default.png");
        	
        	System.out.println("Agreement : "+agreement);
        	System.out.println("User : "+user);
        	User res = this.repo.save(user);
        	m.addAttribute("user", new User());
//        	session.setAttribute("message", new Message("Successfully Registered !! ","alert-success"));
        	
        	 redirectAttributes.addFlashAttribute("message",
                     new Message("Successfully Registered !!", "alert-success"));
        	    return "redirect:/signup";
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		m.addAttribute("user",user);
    		redirectAttributes.addFlashAttribute("message",
                    new Message("Something went wrong: " + e.getMessage(), "alert-danger"));

            return "redirect:/signup";
    	}
    	
    }
    

}
