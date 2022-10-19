package com.vanthan.vn.controller;

import com.vanthan.vn.dto.BaseResponse;
import com.vanthan.vn.dto.EmailDTO;
import com.vanthan.vn.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMailController {

    @Autowired
    SendMailService sendMailService;

    @PostMapping("/sendMail")
    public BaseResponse<String> sendMail(EmailDTO emailDTO){
        return sendMailService.sendMail(emailDTO);
    }
}
