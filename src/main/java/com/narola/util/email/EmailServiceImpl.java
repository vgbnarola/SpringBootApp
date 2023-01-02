package com.narola.util.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private FreeMarkerConfigurer freemarkerConfig;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public boolean sendSimpleMail(EmailDetails details) {
        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            log.warn(e.fillInStackTrace().toString());
            return false;
        }
    }

    @Override
    public boolean sendSimpleHtmlMail(EmailDetails details) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setSubject(details.getSubject());
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());

            String templateContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getConfiguration().getTemplate("forgotpass-template.html"), details.getTemplateData());

            helper.setText(templateContent, true);
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            log.warn(e.fillInStackTrace().toString());
            return false;
        }
    }
}