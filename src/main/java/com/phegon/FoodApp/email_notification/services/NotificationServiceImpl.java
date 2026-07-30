package com.phegon.FoodApp.email_notification.services;

import com.phegon.FoodApp.email_notification.dtos.NotificationDTO;
import com.phegon.FoodApp.email_notification.entity.Notification;
import com.phegon.FoodApp.email_notification.repository.NotificationRepository;
import com.phegon.FoodApp.enums.NotificationType;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private final JavaMailSender javaMailSender; //This is Spring’s email-sending tool.It is used to actually send the email.
    private final NotificationRepository notificationRepository;//This is used to save the notification record into database.,This is the database saving tool.



    @Override
    @Async //Run this method asynchronously, in the background thread.
    public void sendEmail(NotificationDTO notificationDTO) {
        log.info("Inside sendEmail()");
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage(); //This creates an email message object.

            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()); //This helper is like an assistant who helps you fill the email properly.

            helper.setTo(notificationDTO.getRecipient()); //This sets who will receive the email.
            helper.setSubject(notificationDTO.getSubject()); //This sets the email title.
            helper.setText(notificationDTO.getBody(), notificationDTO.isHtml()); //This sets the actual email content.getBody() → main email text,isHtml() → whether body is plain text or HTML

            javaMailSender.send(mimeMessage);//This is the actual sending step.

            //SAVE TO DATABASE
            Notification notificationToSave = Notification.builder()
                    .recipient(notificationDTO.getRecipient())
                    .subject(notificationDTO.getSubject())
                    .body(notificationDTO.getBody())
                    .type(NotificationType.EMAIL)
                    .isHtml(notificationDTO.isHtml())
                    .build(); //Create a Notification entity object using the same data from the DTO.This saved object probably represents a row in your notification table.

            notificationRepository.save(notificationToSave); //This stores the notification in the database.Meaning:Now the notification history is permanently recorded.
            log.info("Saved to notification table");

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

//Why MimeMessage?Because it supports richer email content like:subject,body,HTML,attachments Simple meaning:This is the actual email object you are preparing.
