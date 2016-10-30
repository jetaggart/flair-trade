package com.flairtradetravels;

import org.jinstagram.exceptions.InstagramException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InstagramRealTimeController {

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
            final JSONArray obj = new JSONArray(body);

            return obj.getJSONObject(0).getJSONObject("data").getString("media_id");
        }
    }
}
