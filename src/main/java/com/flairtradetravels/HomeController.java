package com.flairtradetravels;

import com.flairtradetravels.entities.*;
import org.jinstagram.auth.oauth.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

        return new MediaPresenter(instagramMedia, tags);
    }

    private class MediaPresenter {
        private String url;
        private String caption;
        private List<String> tags;

        MediaPresenter(InstagramMedia instagramMedia, List<String> tags) {
            this.url = instagramMedia.getImageUrl();
            this.caption = instagramMedia.getCaption();
            this.tags = tags;
        }

        public String getCost() throws Exception {
            return "$" + parseCost(findFlairTradeTag());
        }

        public String getTime() throws Exception {
            String tag = findFlairTradeTag();

            String days = parseDays(tag);
            String daysString = days != null ? String.format("%s days", days) : "0 days";
            String hours = parseHours(tag);
            String hoursString = hours != null ? String.format("%s hours", hours) : "0 hours";
            String minutes = parseMinutes(tag);
            String minutesString = minutes != null ? String.format("%s minutes", minutes) : "0 minutes";

            return String.format("%s, %s, %s", daysString, hoursString, minutesString);
        }

        private String parseMinutes(String tag) {
            return extractGroup(".*[_|d|h](\\d+)m.*", tag);
        }

        private String parseHours(String tag) {
            return extractGroup(".*[_|d|m](\\d+)h.*", tag);
        }

        private String parseDays(String tag) {
            return extractGroup(".*[_|h|m](\\d+)d.*", tag);
        }

        public String getUrl() {
            return url;
        }

        private String extractGroup(String regex, String tag) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(tag);

            try {
                matcher.matches();
                return matcher.group(1);
            } catch (IllegalStateException ex) {
                return null;
            }
        }

        public List<String> getTags() {
            return tags;
        }

        public String getCaption() {
            return caption;
        }

        private String findFlairTradeTag() throws Exception {
            Optional<String> flairTag = tags.stream()
                .filter(tag -> Pattern.matches("^flairtrade.*$", tag))
                .findFirst();
            if (flairTag.isPresent()) {
                return flairTag.get();
            } else {
                throw new Exception("Should not have a post with no flair tag");
            }
        }

        private String parseCost(String tag) {
            Pattern pattern = Pattern.compile(".*_(\\d+)usd.*");
            Matcher matcher = pattern.matcher(tag);
            matcher.matches();
            return matcher.group(1);
        }

    }
}