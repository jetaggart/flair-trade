package com.flairtradetravels;

import com.flairtradetravels.entities.*;
import org.jinstagram.auth.oauth.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final InstagramService instagramService;
    private final InstagramMediaRepository instagramMediaRepository;
    private final InstagramMediaTagRepository instagramMediaTagRepository;
    private final InstagramTagRepository instagramTagRepository;

    @Autowired
    public HomeController(InstagramService instagramService, InstagramMediaRepository instagramMediaRepository, InstagramMediaTagRepository instagramMediaTagRepository, InstagramTagRepository instagramTagRepository) {
        this.instagramService = instagramService;
        this.instagramMediaRepository = instagramMediaRepository;
        this.instagramMediaTagRepository = instagramMediaTagRepository;
        this.instagramTagRepository = instagramTagRepository;
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("authorizationUrl", instagramService.getAuthorizationUrl());
        model.addAttribute(
            "mediaItems",
            instagramMediaRepository.findAllByOrderByIdDesc().stream().map(this::present).collect(Collectors.toList())
        );

        return "home";
    }

    private MediaPresenter present(InstagramMedia instagramMedia) {
        List<InstagramMediaTag> relationships = instagramMediaTagRepository.findByInstagramMediaId(instagramMedia.getId());
        List<Long> ids = relationships.stream().map(InstagramMediaTag::getInstagramTagId).collect(Collectors.toList());
        List<String> tags = instagramTagRepository
            .findByIdIn(ids)
            .stream()
            .map(InstagramTag::getText)
            .collect(Collectors.toList());

        return new MediaPresenter(instagramMedia.getImageUrl(), tags);
    }

    private class MediaPresenter {
        private String url;
        private List<String> tags;

        MediaPresenter(String url, List<String> tags) {
            this.url = url;
            this.tags = tags;
        }

        public String getUrl() {
            return url;
        }

        public List<String> getTags() {
            return tags;
        }
    }
}