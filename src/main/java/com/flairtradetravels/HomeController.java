package com.flairtradetravels;

import org.jinstagram.auth.oauth.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final InstagramService instagramService;

    @Autowired
    public HomeController(InstagramService instagramService) {
        this.instagramService = instagramService;
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("authorizationUrl", instagramService.getAuthorizationUrl());
        return "home";
    }

}