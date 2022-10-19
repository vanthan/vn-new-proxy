package com.vanthan.vn.service;

import com.vanthan.vn.dto.BaseResponse;
import com.vanthan.vn.dto.EmailDTO;

public interface SendMailService {
    BaseResponse<String> sendMail(EmailDTO emailDTO);
}
