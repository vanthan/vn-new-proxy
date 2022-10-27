package com.vanthan.vn.service;

import com.vanthan.vn.dto.BaseResponse;
import com.vanthan.vn.dto.RegisterForm;
import com.vanthan.vn.dto.RegisterResult;
import com.vanthan.vn.model.User;
import org.springframework.stereotype.Service;


public interface ImpAuthen {

    BaseResponse<RegisterResult> register(RegisterForm body);

}
