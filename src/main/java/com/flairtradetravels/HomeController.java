package com.flairtradetravels;

import com.flairtradetravels.entities.InstagramMediaRepository;
import org.jinstagram.auth.oauth.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final InstagramService instagramService;
    private final InstagramMediaRepository instagramMediaRepository;

    @Autowired
    public HomeController(InstagramService instagramService, InstagramMediaRepository instagramMediaRepository) {
        this.instagramService = instagramService;
        this.instagramMediaRepository = instagramMediaRepository;
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("authorizationUrl", instagramService.getAuthorizationUrl());
        model.addAttribute("mediaItems", instagramMediaRepository.findAll());

        return "home";
    }

}