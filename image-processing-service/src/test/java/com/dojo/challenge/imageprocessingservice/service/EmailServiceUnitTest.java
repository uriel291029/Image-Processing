package com.dojo.challenge.imageprocessingservice.service;

import com.dojo.challenge.imageprocessingservice.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class EmailServiceUnitTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    public void testSendEmail() throws MessagingException {
        String text = "Text";
        MimeMessage mimeMessage = new MimeMessage((Session)null);
        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService.sendWithAttach("gus@gmail.com", Collections.singletonList("jhon@gmail.com").toArray(new String[0]),
                "Hello", "How are you?", "info.txt", new ByteArrayResource(text.getBytes()));
    }
}
