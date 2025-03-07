package com.myProject.OpenBoard.controller;


import com.myProject.OpenBoard.entity.Post;
import com.myProject.OpenBoard.entity.Role;
import com.myProject.OpenBoard.entity.User;
import com.myProject.OpenBoard.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SecurityController {

    private MainService mainService;

    @Autowired
    public SecurityController(MainService mainService) {
        this.mainService = mainService;
    }


    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/accessDenied")
    public String accessDenied(Model model){

        List<Post> postList = mainService.findAllUserPost();

        model.addAttribute("postList", postList);

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = mainService.findUserByName(username);

        List<Role> roleList = user.getRoleList();
        List<String> roleNameList = new ArrayList<>();

        for(Role x : roleList){
            roleNameList.add(x.getRole());
        }

        model.addAttribute("accessDeny",
                "Sorry! You cannot access this with your role as " + roleNameList + " !!!");
        return "home";
    }
}
