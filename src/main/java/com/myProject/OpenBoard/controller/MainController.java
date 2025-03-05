package com.myProject.OpenBoard.controller;

import com.myProject.OpenBoard.entity.Post;
import com.myProject.OpenBoard.entity.User;
import com.myProject.OpenBoard.service.MainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.Banner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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
        if(!postList.isEmpty()){
            model.addAttribute("postList", postList);
            return "home";
        }
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


    @GetMapping("/post")
    public String showPost(Model model){
        model.addAttribute("post", new Post());
        model.addAttribute("isUpdate", false);
        return "post";
    }


    @PostMapping("/processPost")
    public String postProcess(@Valid @ModelAttribute("post")Post post,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes)
    {
        if(bindingResult.hasErrors()){
            model.addAttribute("isUpdate", false);
            return "post";
        }
        mainService.createPost(post);
        redirectAttributes.addFlashAttribute("success", "Post created successful!");
        return "redirect:/home";
    }


    @GetMapping("/updatePost")
    public String updatePost(@RequestParam("id")int id, Model model){
        Post selectedPost = mainService.findPostById(id);
        model.addAttribute("post", selectedPost );
        model.addAttribute("isUpdate", true);
        return "post";
    }

    @GetMapping("/deletePost")
    public String deletePost(@RequestParam("id")int id, Model model, RedirectAttributes redirectAttributes){
        mainService.deletePostById(id);
        List<Post> postList = mainService.findAllUserPost();
        model.addAttribute("postList", postList);
        redirectAttributes.addFlashAttribute("success", "Post deleted Successfully");
        return "redirect:/home";
    }

    @PostMapping("/processUpdatePost")
    public String postUpdateProcess(@Valid @ModelAttribute("post")Post post,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes)
    {
        if(bindingResult.hasErrors()){
            model.addAttribute("isUpdate", true);
            return "post";
        }
        mainService.updatePost(post);
        redirectAttributes.addFlashAttribute("success", "Post updated successful!");
        return "redirect:/home";
    }

    @GetMapping("/publicPost")
    public String showPublicPost(Model model){
        List<Post> publicPost = mainService.findPublicPost();
        model.addAttribute("publicPost", publicPost);
        return "publicPost";
    }


    @GetMapping("/profile")
    public String showProfile(Model model) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = mainService.findUserByName(username);
        model.addAttribute("user", user);
        return "profile";
    }


    @PostMapping("/processProfile")
    public String processProfile(@Valid @ModelAttribute("user") User updatedUser,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        mainService.updateUser(updatedUser);
        redirectAttributes.addFlashAttribute("success", "User Profile Updated successfully!!");
        return "redirect:/profile";
    }



    @PostMapping("/processProfilePassword")
    public String changePassword(@Valid @ModelAttribute("user") User updatedUser,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes)
    {
        if(bindingResult.hasErrors()){
            return "profile";
        }
        boolean doseMatch = mainService.updatePassword(updatedUser, currentPassword, newPassword);
        if(doseMatch){
            redirectAttributes.addFlashAttribute("result", "Password Changed successfully !!");
        }else{
            redirectAttributes.addFlashAttribute("result", "Current Password dose not match");
        }
        return "redirect:/profile";
    }




}
