package com.example.mymoo.domain.email;

import com.example.mymoo.domain.donationusage.entity.DonationUsage;
import com.example.mymoo.domain.email.dto.EmailSendDTO;
import com.example.mymoo.domain.email.exception.EmailException;
import com.example.mymoo.domain.email.exception.EmailExceptionDetails;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailClient {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendDonationUsageMail(EmailSendDTO send){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(send.getEmail());
            mimeMessageHelper.setSubject("[마이무] 감사합니다! 당신의 후원이 사용되었습니다!");
            mimeMessageHelper.setText(setContext("email", send), true);
            javaMailSender.send(mimeMessage);
            log.info("이메일 전송 성공");
        } catch (MessagingException e){
            log.info("이메일 전송 실패");
            throw new EmailException(EmailExceptionDetails.EMAIL_SEND_FAILED);
        }
    }

    public String setContext(String type, EmailSendDTO send){
        Context context = new Context();
        context.setVariable("storeName", send.getStoreName());
        context.setVariable("usedPrice", send.getUsedPrice());
        context.setVariable("childName", send.getChildName());
        context.setVariable("usedTime", send.getUsedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        context.setVariable("donatedTime", send.getDonatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return templateEngine.process(type, context);
    }


}
