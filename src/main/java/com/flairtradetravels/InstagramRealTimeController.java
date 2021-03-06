package com.flairtradetravels;

import com.flairtradetravels.entities.*;
import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.regex.Pattern;

@Controller
public class InstagramRealTimeController {
    private final InstagramMediaRepository instagramMediaRepository;
    @Value("${instagram.appToken}")
    private String appToken;

    private InstagramTagRepository instagramTagRepository;
    private InstagramMediaTagRepository instagramMediaTagRepository;

    @Autowired
    public InstagramRealTimeController(InstagramMediaRepository instagramMediaRepository, InstagramService instagramService, InstagramTagRepository instagramTagRepository, InstagramMediaTagRepository instagramMediaTagRepository) {
        this.instagramMediaRepository = instagramMediaRepository;
        this.instagramTagRepository = instagramTagRepository;
        this.instagramMediaTagRepository = instagramMediaTagRepository;
    }
    // [{"changed_aspect": "media", "object": "user", "object_id": "190277687", "time": 1477869300, "subscription_id": 0, "data": {"media_id": "1372789645779973559_190277687"}}]

    @RequestMapping(value = "/api/v1/instagram/realtime")
    @ResponseBody
    public String instagramToken(@RequestParam(value = "hub.challenge", required = false) String challenge,
                                 @RequestParam(value = "hub.mode", required = false) String mode,
                                 @RequestParam(value = "hub.verify_token", required = false) String verifyToken,
                                 @RequestBody String body) throws InstagramException {
        if (challenge != null) {
            return challenge;
        } else {
            final JSONArray mediaObjects = new JSONArray(body);

            for (int i = 0; i < mediaObjects.length(); i++) {
                JSONObject mediaObject = mediaObjects.getJSONObject(i);
                String instagramId = mediaObject.getJSONObject("data").getString("media_id");

                Token token = new Token(appToken, null);
                Instagram instagram = new Instagram(token);

                MediaFeedData data = instagram.getMediaInfo(instagramId).getData();

                List<String> tags = data.getTags();

                if (hasFlairTradeTags(tags)) {
                    createMedia(instagramId, data, tags);
                }
            }

            return "";
        }
    }

    private void createMedia(String instagramId, MediaFeedData data, List<String> tags) {
        InstagramMedia instagramMedia = instagramMediaRepository.save(
            new InstagramMedia(
                instagramId,
                data.getImages().getStandardResolution().getImageUrl(),
                data.getCaption().getText()
            )
        );

        tags.forEach(tagText -> findOrCreateTag(tagText, instagramMedia));
    }

    private boolean hasFlairTradeTags(List<String> tags) {
        return tags.stream().anyMatch(
            tag -> Pattern.matches("^flairtrade.*$", tag)
        );
    }

    private void findOrCreateTag(String tagText, InstagramMedia media) {
        List<InstagramTag> result = instagramTagRepository.findByText(tagText);

        InstagramTag instagramTag;
        if (result.isEmpty()) {
            instagramTag = instagramTagRepository.save(new InstagramTag(tagText));
        } else {
            instagramTag = result.get(0);
        }

        instagramMediaTagRepository.save(
            new InstagramMediaTag(media.getId(), instagramTag.getId())
        );
    }
}
