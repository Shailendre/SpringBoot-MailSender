package com.thermofisher;

import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 *  Created by shailendra.singh on 3/13/17.
 */
@RestController
@RequestMapping("/mail")
public class SendMailController {

    {
        initConfiguration();
    }

    @Autowired
    private JavaMailSender mailSender;

    private Configuration configuration;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${firstname}")
    private String myname;

    @Value("${mailsubject}")
    private String mailsubject;

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailController.class);

    @RequestMapping(value = "/send", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public boolean sendMessage(@QueryParam("to") String to, @QueryParam("msg") String msg,
                               @QueryParam("applink") String applink) {

        boolean sent = false;
        LOGGER.info("mail configured");
        LOGGER.info("to: {} msg : {}", to, msg);
        try {
            Arrays.asList(to.split(";")).forEach(recipient -> {
                MimeMessagePreparator mimeMessagePreparator = getMimeMessagePreparator(recipient, msg, applink);
                mailSender.send(mimeMessagePreparator);
            });
            LOGGER.info("mail sent");
            sent = true;
        } catch (Exception ex) {
            LOGGER.debug("MailSendingException: " + ex.getMessage());
            sent = false;
        } finally {
            return sent;
        }
    }


    private MimeMessagePreparator getMimeMessagePreparator(String to, String msg, String applink) {

        MimeMessagePreparator mimeMessagePreparator = (mimeMessage) -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mailsubject);
            mimeMessageHelper.setFrom(username);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setText(getTemplateEnabledMessageBody(getTemplateModelMap(to, msg, applink)), true);
        };
        return mimeMessagePreparator;
    }

    private String getTemplateEnabledMessageBody(Map<String, Object> templateMap) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    configuration.getTemplate("fm-mail-template.ftl"), templateMap));
            return stringBuilder.toString();
        } catch (Exception e) {
            LOGGER.debug("Exception occured while processing fmtemplate: " + e.getMessage());
        }
        return "";
    }

    private Map<String, Object> getTemplateModelMap(String to, String msg, String appLink) {
        Map<String, Object> map = new HashMap<>();
        map.put("recipient", to.split("@")[0].toUpperCase());
        map.put("applink", appLink);
        map.put("msg", StringUtils.isEmpty(msg) ? "" : msg);
        map.put("myname", myname);
        return map;
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void initConfiguration() {
        configuration = new Configuration();
        configuration.setClassForTemplateLoading(this.getClass(), "/templates/");
    }


    @RequestMapping(value = "/healthcheck" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String healthCheck(){
        return "OK";
    }


}
