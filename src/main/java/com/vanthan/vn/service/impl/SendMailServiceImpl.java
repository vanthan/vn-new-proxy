package com.vanthan.vn.service.impl;

import com.vanthan.vn.dto.BaseResponse;
import com.vanthan.vn.dto.EmailDTO;
import com.vanthan.vn.dto.ProductDetailDTO;
import com.vanthan.vn.service.SendMailService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;
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
//            TransactionDetail transactionDetail = new TransactionDetail();
//            String mail = transactionDetail.getEmail();
            mailMessage.setTo("nttinh1@cmcglolbal.vn");
            mailMessage.setSubject(emailDTO.getSubject());
            mailMessage.setText(emailDTO.getMessage());

            javaMailSender.send(mailMessage);
            response.setMessage("Send mail successfully!!");
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
            response.setMessage("Send mail fail" + ex.getMessage());
        }
        return response;
    }

    @Override
    public BaseResponse<String> sendMail2(EmailDTO emailDTO) throws MessagingException {
        BaseResponse response = new BaseResponse<String>();
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Context context = new Context();

            List<ProductDetailDTO> productDetailDTOS = new ArrayList<ProductDetailDTO>();
            ProductDetailDTO sp1 = new ProductDetailDTO();
            sp1.setProductName("sp1");
            sp1.setQty("123");
            sp1.setTotal("999");
            productDetailDTOS.add(sp1);

            ProductDetailDTO sp2 = new ProductDetailDTO();
            sp2.setProductName("sp2");
            sp2.setQty("234");
            sp2.setTotal("999");
            productDetailDTOS.add(sp2);

            // Set Variables
            Map<String, Object> props = new HashMap<String, Object>();
            props.put("fullName", "Nguyen Thi Tinh!");
            props.put("product", productDetailDTOS);
            props.put("total", "123");
            props.put("status", "status");
            props.put("paymentMethod", "paymentMethod");
            emailDTO.setProps(props);
            // Set Variables

            context.setVariables(emailDTO.getProps());

            final String template = "mail-template";
            String html = templateEngine.process(template, context);

            helper.setTo(emailDTO.getRecipient());
            helper.setText(html, true);
            helper.setSubject(emailDTO.getSubject());
            emailSender.send(message);
            response.setMessage("Send mail successfully!!");
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
            ex.printStackTrace();
            response.setCode("001");
            response.setMessage("Send mail fail" + ex.getMessage() + ex.getStackTrace() );
        }
        return response;
    }

}
