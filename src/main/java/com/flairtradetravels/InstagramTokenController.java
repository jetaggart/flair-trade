package com.flairtradetravels;

import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class InstagramTokenController {

    private final InstagramService instagramService;
    private final HttpSession session;

    @Autowired public InstagramTokenController(InstagramService instagramService, HttpSession session) {
        this.instagramService = instagramService;
        this.session = session;
    }

    @GetMapping("/instagram/token")
    public String instagramToken(@RequestParam(value = "code") String code, Model model) throws InstagramException {
        System.out.println(code);
        Verifier verifier = new Verifier(code);
        Token accessToken = instagramService.getAccessToken(verifier);

        session.setAttribute("instagramToken", accessToken);

        return "redirect:/pictures";
    }
}
