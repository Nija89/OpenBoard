package com.myProject.OpenBoard.controller;

import com.myProject.OpenBoard.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ModeratorController {

    private MainService mainService;

    @Autowired
    public ModeratorController(MainService mainService) {
        this.mainService = mainService;
    }


    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    @GetMapping("/deletePublicPost")
    public String deletePublicPost(@RequestParam("id") int id, RedirectAttributes redirectAttributes){
        mainService.deletePublicPostById(id);

        redirectAttributes.addFlashAttribute("successDelete", "Successfully Deleted a Public Post!");
        return "redirect:/publicPost";
    }
}
