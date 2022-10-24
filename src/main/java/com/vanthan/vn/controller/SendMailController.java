package com.vanthan.vn.controller;

import com.vanthan.vn.dto.BaseResponse;
import com.vanthan.vn.dto.EmailDTO;
import com.vanthan.vn.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
public class SendMailController {

    @Autowired
    SendMailService sendMailService;

    @PostMapping("sendMail")
    public BaseResponse<String> sendMail(@RequestBody EmailDTO emailDTO) throws MessagingException {
        return sendMailService.sendMail2(emailDTO);
    }
}
