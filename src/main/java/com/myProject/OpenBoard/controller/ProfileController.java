package com.myProject.OpenBoard.controller;

import com.myProject.OpenBoard.entity.User;
import com.myProject.OpenBoard.service.MainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {
    private MainService mainService;

    @Autowired
    public ProfileController(MainService mainService) {
        this.mainService = mainService;
    }


    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
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
