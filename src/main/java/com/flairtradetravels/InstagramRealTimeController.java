package com.flairtradetravels;

import com.flairtradetravels.entities.InstagramMedia;
import com.flairtradetravels.entities.InstagramMediaRepository;
import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.oauth.InstagramService;
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

import javax.servlet.http.HttpSession;

@Controller
public class InstagramRealTimeController {
    private final InstagramMediaRepository instagramMediaRepository;
    private final InstagramService instagramService;
    @Value("${instagram.appToken}")
    private String appToken;

    @Autowired
    public InstagramRealTimeController(InstagramMediaRepository instagramMediaRepository, InstagramService instagramService) {
        this.instagramMediaRepository = instagramMediaRepository;
        this.instagramService = instagramService;
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
                String imageUrl = instagram.getMediaInfo(instagramId).getData().getImages().getStandardResolution().getImageUrl();

                instagramMediaRepository.save(new InstagramMedia(instagramId, imageUrl));
            }

            return "";
        }
    }
}
