package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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
        
        String MD5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,MD5Password);
        if (user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        //密码置为空 再返还
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse vaildResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if (!vaildResponse.isSuccess()){
            return vaildResponse;
        }
        vaildResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if (!vaildResponse.isSuccess()){
            return vaildResponse;
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int insertCount = userMapper.insert(user);
        if (insertCount == 0 ){
            return ServerResponse.createBySuccessMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }


    @Override
    /**
     * 校验输入的有效性
     */
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)){
            if (type.equals(Const.USERNAME)){
                int returnCount = userMapper.checkUsername(str);
                if (returnCount > 0){
                    return  ServerResponse.createByErrorMessage("用户名已存在");
                }
            }else if (type.equals(Const.EMAIL)){
                int returnCount = userMapper.checkEmail(str);
                if (returnCount > 0){
                    return  ServerResponse.createByErrorMessage("email已存在");
                }
            }else {
                return ServerResponse.createByErrorMessage("参数不正确");
            }
        } else {
            return ServerResponse.createByErrorMessage("参数不正确");
        }
        return  ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }
}
