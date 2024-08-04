package com.smart.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Model model) {
		
		model.addAttribute("title","Home - Smart Contact Manager");
		logger.info("Accessed home page");
		return "home";
	}
	
	 @GetMapping("/signup")
	    public String signup(Model model) {
	        model.addAttribute("title", "Sign Up");
	        model.addAttribute("user", new User());
	        logger.info("Accessed signup page");
	        return "signup";
	    }
	@RequestMapping("/about")
	public String about(Model model) {
		
		model.addAttribute("title","About - Smart Contact Manager");
		 logger.info("Accessed about page");
		return "about";
	}
	
	@RequestMapping(value="/do_register",method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user")User user, Model model, BindingResult result,HttpSession session) {
		logger.info("Registering user: {}", user.getEmail());
		try {
			if(result.hasErrors()) {
				 logger.warn("Registration form has errors: {}", result.toString());
				model.addAttribute("user",user);
				return "signup";
			}
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageurl("default.png");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User result1=this.userRepository.save(user);
		logger.info("User registered successfully: {}", result1.getEmail());
		model.addAttribute("user",new User());
		session.setAttribute("message", new Message("Successfully Registered","alert-success"));
		return "signup";
		}catch(Exception e) {
			 logger.error("Error registering user: {}", user.getEmail(), e);
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong","alert-danger"));
			return "signup";
		}
	}
	@RequestMapping("/signin")
	public String login(Model model) {
		model.addAttribute("title","Login Page");
		 logger.info("Accessed login page");
		return "signin";	
	}
}
