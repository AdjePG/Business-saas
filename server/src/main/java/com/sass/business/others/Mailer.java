package com.sass.business.others;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class Mailer {
    // region INJECTED DEPENDENCIES

    private JavaMailSender mailSender;
    private SpringTemplateEngine templateEngine;

    public Mailer(
            JavaMailSender mailSender,
            SpringTemplateEngine templateEngine
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // endregion

    public void sendEmail(String to, String subject, String source, boolean isHTML, Map<String, String> variables) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String text;

        if (isHTML) {
            text = getHTMLtemplate(source, variables);
        } else {
            text = source;
        }

        helper.setTo(to.split(","));
        helper.setSubject(subject);
        helper.setText(text, isHTML);

        mailSender.send(message);
    }

    private String getHTMLtemplate(String path, Map<String, String> variables) {
        Context context = new Context();

        if (variables != null) {
            for (Map.Entry<String, String> variable : variables.entrySet()) {
                context.setVariable(variable.getKey(), variable.getValue());
            }
        }

        return templateEngine.process(path, context);
    }
}
