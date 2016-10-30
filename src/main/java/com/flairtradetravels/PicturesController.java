package com.flairtradetravels;

import org.jinstagram.Instagram;
import org.jinstagram.InstagramOembed;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.oembed.OembedInformation;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PicturesController {

    private final InstagramService instagramService;
    private final HttpSession session;

    @Autowired
    public PicturesController(InstagramService instagramService, HttpSession session) {
        this.instagramService = instagramService;
        this.session = session;
    }

    @RequestMapping("/pictures")
    public String home(Model model) throws InstagramException {
        Token instagramToken = (Token) session.getAttribute("instagramToken");
        Instagram instagram = new Instagram(instagramToken);

        String fullName = instagram.getCurrentUserInfo().getData().getFullName();
        String username = instagram.getCurrentUserInfo().getData().getId();

        List<MediaFeedData> mediaFeed = instagram.getRecentMediaFeed(username).getData();

        String url = mediaFeed.get(0).getLink();
        model.addAttribute("url", mediaFeed.get(0).getImages().getStandardResolution().getImageUrl());
        model.addAttribute("caption", mediaFeed.get(0).getCaption().getText());
        model.addAttribute("tags", mediaFeed.get(0).getTags());
        model.addAttribute("name", fullName + username);


        return "/pictures";
    }

}