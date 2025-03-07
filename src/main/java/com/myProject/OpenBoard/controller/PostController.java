package com.myProject.OpenBoard.controller;

import com.myProject.OpenBoard.entity.Post;
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
public class PostController {

    private MainService mainService;

    @Autowired
    public PostController(MainService mainService) {
        this.mainService = mainService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
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

    @GetMapping("/deletePost")
    public String deletePost(@RequestParam("id")int id, Model model, RedirectAttributes redirectAttributes){
        mainService.deletePostById(id);

        redirectAttributes.addFlashAttribute("success", "Post deleted Successfully");
        return "redirect:/home";
    }







    @GetMapping("/publicPost")
    public String showPublicPost(Model model){
        List<Post> publicPost = mainService.findPublicPost();

        model.addAttribute("publicPost", publicPost);
        return "publicPost";
    }

    @GetMapping("/like")
    public String like(@RequestParam("id")int id, RedirectAttributes redirectAttributes){
        mainService.likePublicPost(id);

        redirectAttributes.addFlashAttribute("success",
                "Successfully Liked " + mainService.findPostById(id).getTitle());
        return "redirect:/publicPost";
    }


    @GetMapping("/dislike")
    public String dislike(@RequestParam("id")int id, RedirectAttributes redirectAttributes){
        mainService.disLikePublicPost(id);

        redirectAttributes.addFlashAttribute("success",
                "Successfully Disliked " + mainService.findPostById(id).getTitle());

        return "redirect:/publicPost";
    }






    @GetMapping("/topTenPost")
    public String topTenPost(Model model){
        model.addAttribute("topPost", mainService.topTenPost());
        return "TopPost";
    }
}
