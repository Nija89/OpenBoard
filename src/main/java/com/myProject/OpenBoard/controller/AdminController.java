package com.myProject.OpenBoard.controller;

import com.myProject.OpenBoard.entity.User;
import com.myProject.OpenBoard.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {
    private MainService mainService;

    @Autowired
    public AdminController(MainService mainService) {
        this.mainService = mainService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/viewUser")
    public String getAllUser(Model model){
        List<User> userList= mainService.findAllMemberUser();
        List<User> moderatorList = mainService.findAllModeratorUser();
        List<User> disabledUserList = mainService.findDisabledUser();

        model.addAttribute("userList", userList);
        model.addAttribute("moderatorList", moderatorList);
        model.addAttribute("disabledUserList", disabledUserList);

        return "Users";
    }

    @GetMapping("/promote")
    public String promoteUser(@RequestParam("id")int id, RedirectAttributes redirectAttributes){
        mainService.promoteUserById(id);

        redirectAttributes.addFlashAttribute("success", "Successfully Promoted to Moderator");
        return "redirect:/viewUser";
    }

    @GetMapping("/demote")
    public String demoteModerator(@RequestParam("id")int id, RedirectAttributes redirectAttributes){
        mainService.demoteUserById(id);

        redirectAttributes.addFlashAttribute("success", "Successfully Demoted to Member");
        return "redirect:/viewUser";
    }

    @GetMapping("/enableUser")
    public String enableUser(@RequestParam("id")int id, RedirectAttributes redirectAttributes){
        mainService.enableUserById(id);

        redirectAttributes.addFlashAttribute("success", "Successfully Enabled.");
        return "redirect:/viewUser";
    }
    @GetMapping("/disableUser")
    public String disableUser(@RequestParam("id")int id, RedirectAttributes redirectAttributes){
        mainService.disableUserById(id);

        redirectAttributes.addFlashAttribute("success", "Successfully Disabled.");
        return "redirect:/viewUser";
    }



    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("id")int id, RedirectAttributes redirectAttributes){
        mainService.deleteUserById(id);

        redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        return "redirect:/viewUser";
    }




}


