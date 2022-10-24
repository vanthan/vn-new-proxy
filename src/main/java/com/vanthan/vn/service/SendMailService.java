package com.vanthan.vn.service;

import com.vanthan.vn.dto.BaseResponse;
import com.vanthan.vn.dto.EmailDTO;

import javax.mail.MessagingException;

public interface SendMailService {
    BaseResponse<String> sendMail(EmailDTO emailDTO);

    BaseResponse<String> sendMail2(EmailDTO emailDTO) throws MessagingException;

}
