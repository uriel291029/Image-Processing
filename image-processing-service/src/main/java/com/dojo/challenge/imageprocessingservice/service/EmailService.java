package com.dojo.challenge.imageprocessingservice.service;

import org.springframework.core.io.InputStreamSource;

import javax.mail.MessagingException;

public interface EmailService {

    void sendWithAttach(String from, String[] to, String subject,
                        String text, String attachName,
                        InputStreamSource inputStream) throws MessagingException;
}
