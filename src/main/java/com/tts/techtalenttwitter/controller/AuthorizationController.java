package com.tts.techtalenttwitter.controller;

import javax.validation.Valid;

import com.tts.techtalenttwitter.model.User;
import com.tts.techtalenttwitter.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

// handles web requests --> Controller

@Controller
public class AuthorizationController {

    @Autowired
    private UserService userService;

    // Goes to login form
    @GetMapping(value = "/login")
    public String login() {
        return "login"; // return value is the name of template for webpage
    }

    // Goes to registration form 
    @GetMapping(value = "/signup")
    public String registration(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    // Making sure we sign up properly
    // error if username is already taken, success if it was successful
    @PostMapping(value = "/signup")
    public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
        User userExists = userService.findByUsername(user.getUsername());
        if (userExists != null) {
            // User already exists so send error
            bindingResult.rejectValue("username", "error.user", "Username is already taken");
        }
        // user has no errors...
        if (!bindingResult.hasErrors()) {
            // process a new user to database
            userService.saveNewUser(user);
            model.addAttribute("success", "Sign up successful!");
            // makes the form a blank field again
            model.addAttribute("user", new User());
        }
        return "registration";
    }

}
