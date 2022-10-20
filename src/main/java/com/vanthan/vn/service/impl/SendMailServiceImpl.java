package com.vanthan.vn.service.impl;

import com.vanthan.vn.dto.BaseResponse;
import com.vanthan.vn.dto.EmailDTO;
import com.vanthan.vn.model.TransactionDetail;
import com.vanthan.vn.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailServiceImpl implements SendMailService {
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public BaseResponse<String> sendMail(EmailDTO emailDTO) {
        BaseResponse response = new BaseResponse<String>();
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            TransactionDetail transactionDetail = new TransactionDetail();
            String mail = transactionDetail.getEmail();
            mailMessage.setTo(mail);
            mailMessage.setSubject(emailDTO.getSubject());
            mailMessage.setText(emailDTO.getMessage());

            javaMailSender.send(mailMessage);
            response.setMessage("Send mail successfully!!");
        } catch (Exception ex) {
            response.setMessage("Send mail fail");
        }
        return response;
    }

}
