package com.myProject.OpenBoard.controller;

import com.myProject.OpenBoard.entity.Post;
import com.myProject.OpenBoard.entity.User;
import com.myProject.OpenBoard.service.MainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MainController {

    private MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }


    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    @GetMapping("/")
    private String showPage(){
        return "landingPage";
    }


    @GetMapping("/home")
    public String homePage(Model model){
        List<Post> postList = mainService.findAllUserPost();
        model.addAttribute("postList", postList);
        return "home";
    }


    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }


    @GetMapping("/register")
    public String signup(Model model){
        model.addAttribute("newUser", new User());
        return "signup";
    }


    @PostMapping("/processSignupForm")
    public String processSignupForm(@Valid @ModelAttribute("newUser") User newUser,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes)
    {
        if(bindingResult.hasErrors()){
            return "signup";
        }
        mainService.createUser(newUser);
        redirectAttributes.addFlashAttribute("success", "Signup successful! You can now log in.");
        return "redirect:/login";
    }



}
