package com.thermofisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *  Created by shailendra.singh on 3/20/17.
 */
@RestController
@RequestMapping("/twitter")
public class SendTwitterPostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendTwitterPostController.class);

    @Value("${twitter.consumerkey}")
    private String consumerKey;

    @Value("${twitter.consumersecret}")
    private String consumerSecretKey;

    @Value("${twitter.accesstoken}")
    private String accessToken;

    @Value("${twitter.accesstokensecret}")
    private String accessTokenSecret;

    @RequestMapping(value = "/post", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public boolean sendPost(@QueryParam("msg") String msg, @QueryParam("applink") String applink){
        boolean sent = false;

        try {

            Twitter twitter = TwitterFactory.getSingleton();

            twitter.setOAuthConsumer(consumerKey, consumerSecretKey);
            twitter.setOAuthAccessToken( new AccessToken(accessToken, accessTokenSecret));
            LOGGER.debug("Twitter : ");

            StatusUpdate statusUpdateMsg = new StatusUpdate(msg+"\nPlease try out: "+applink);
            twitter.updateStatus(statusUpdateMsg);

            sent = true;

        } catch (TwitterException ex) {
            LOGGER.info("Twitter Exception: " + ex.getMessage());
            sent = false;
        } finally {
            return sent;
        }
    }

    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String healthCheck(){
        return "OK";
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecretKey() {
        return consumerSecretKey;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }
}
