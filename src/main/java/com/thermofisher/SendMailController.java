package com.thermofisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


/**
 *  Created by shailendra.singh on 3/13/17.
 */
@RestController
@RequestMapping("/api")
public class SendMailController {

    @Autowired
    private MailSender mailSender;

    private SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailController.class);

    @RequestMapping(value = "/sendmsg", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String sendMessage(@QueryParam("to") String to, @QueryParam("msg") String msg){

        simpleMailMessage.setSubject("~-Test-~");
        simpleMailMessage.setText(msg);
        simpleMailMessage.setFrom("myemail@gmail.com");
        simpleMailMessage.setTo(to.split(";"));

        mailSender.send(simpleMailMessage);

        return "Mail Sent";

    }

    @RequestMapping(value = "/hello" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getHello(){
        return "Hello";
    }


}
