package com.ironmind.ferrus.controllers;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.simplejavamail.util.ConfigLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static javax.mail.Message.RecipientType.BCC;
import static org.simplejavamail.mailer.config.TransportStrategy.SMTP_TLS;


@Controller
public class MailController{
    @GetMapping("/sendmail/{id}")
    public String testMail(@PathVariable long id){
        //ConfigLoader.loadProperties("simplejavamail.properties"); // optional default
        //ConfigLoader.loadProperties("overrides.properties"); // optional extra

        Email email = EmailBuilder.startingBlank()
                .from("Thomas Meyer", "naxral@hotmail.com")
                .to("Noah", "noahjdc92@gmail.com")
                .to("John", "jnwywe@gmail.com")
                .withSubject("John just pissed himself")
                .withPlainText("omg so gross guyz.  i dun even")
                .buildEmail();

        MailerBuilder
                .withSMTPServer("smtp.live.com", 587, "naxral@hotmail.com", "Luvlove2!")
                .withTransportStrategy(SMTP_TLS)
                .buildMailer()
                .sendMail(email);



        return "/";

    }
}