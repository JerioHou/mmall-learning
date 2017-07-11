package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/11.
 */

public interface IUserService {
    ServerResponse<User> login(String username, String password);

}
