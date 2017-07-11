package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/11.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultcount = userMapper.checkUsername(username);
        if (resultcount == 0){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        
//        String MD5Password = MD5

        User user = userMapper.selectLogin(username,password);
        if (user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        //密码置为空 再返还
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }
}
