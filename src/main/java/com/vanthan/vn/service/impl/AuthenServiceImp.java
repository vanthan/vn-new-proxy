package com.vanthan.vn.service.impl;

import com.vanthan.vn.dto.BaseResponse;
import com.vanthan.vn.dto.RegisterForm;
import com.vanthan.vn.dto.RegisterResult;
import com.vanthan.vn.dto.UserResult;
import com.vanthan.vn.jwt.JwtUtils;
import com.vanthan.vn.model.User;
import com.vanthan.vn.model.UserToken;
import com.vanthan.vn.repository.UserRepository;
import com.vanthan.vn.repository.UserTokenRespository;
import com.vanthan.vn.service.ImpAuthen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenServiceImp implements ImpAuthen {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenRespository tokenRespository;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public BaseResponse<RegisterResult> register(RegisterForm body) {
        User user = new User();
        BaseResponse response = new BaseResponse<RegisterResult>();
        //check user by user name
        user = userRepository.findByUsername(body.getUsername());
        if (user != null){
            response.setCode("001");
            response.setMessage("User already existed");
            return response;
        }

        //create new user
        user = new User();
        user.setFullName(body.getFullName());
        user.setEmail(body.getEmail());
        user.setUsername(body.getUsername());
        user.setPassword(body.getPassword());
        userRepository.save(user);
        // result
        response.setCode("00");
        response.setMessage("Successfully created a new user!");

        //create token, add token
        RegisterResult registerResult = new RegisterResult();
        // result token
        UserResult userResult = new UserResult();
        userResult.setUserName(body.getUsername());
        userResult.setUserId(user.getId());
        userResult.setEmail(user.getEmail());
        userResult.setPassword(user.getPassword());
        registerResult.setToken(jwtUtils.generateJwtToken(userResult));
        UserToken userToken = new UserToken();
        userToken.setToken(registerResult.getToken());
        userToken.setUserId(user.getId());
        tokenRespository.save(userToken);

        response.setBody(registerResult);

        return response;
    }
}
