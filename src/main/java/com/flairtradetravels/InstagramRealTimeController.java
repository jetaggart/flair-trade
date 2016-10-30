package com.flairtradetravels;

import org.jinstagram.exceptions.InstagramException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InstagramRealTimeController {

    @RequestMapping(value = "/api/v1/instagram/realtime")
    @ResponseBody
    public String instagramToken(@RequestParam(value = "hub.challenge", required = false) String challenge,
                                 @RequestParam(value = "hub.mode", required = false) String mode,
                                 @RequestParam(value = "hub.verify_token", required = false) String verifyToken) throws InstagramException {
        return challenge;
    }
}
