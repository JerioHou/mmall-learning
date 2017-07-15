package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse vaildResponse = this.checkValid(username,Const.USERNAME);
        if (vaildResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("密保问题不存在");
    }

    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if (resultCount > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案不正确");
    }

    //// TODO: 2017/7/15        forgetResetPassword
    public ServerResponse<String> forgetResetPassword(String username,String password,String forgetToken){
        if (StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("需要传入token");
        }
        ServerResponse vaildResponse = this.checkValid(username,Const.USERNAME);
        if (vaildResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String key = TokenCache.TOKEN_PREFIX+username;
        String token = TokenCache.getKey(key);
        if (StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if (StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(password);
            int resultCount = userMapper.updatePasswordByUsername(username,md5Password);
            if (resultCount > 0){
                return ServerResponse.createBySuccessMessage("密码修改成功");
            }else{
                return ServerResponse.createByErrorMessage("token错误");
            }
        }
        return ServerResponse.createByErrorMessage("密码修改失败");
    }

    //// TODO: 2017/7/15        resetPassword
    public ServerResponse<String> resetPassword(User user ,String passwordOld,String passwordNew){
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if (resultCount == 0 ){
            return ServerResponse.createByErrorMessage("旧密码不正确");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0 ){
            return  ServerResponse.createBySuccessMessage("密码修改成功");
        }
        return ServerResponse.createByErrorMessage("密码修改失败");
    }
    //// TODO: 2017/7/15        updateInformation
    public ServerResponse<User> updateInformation(User user){
        int resuleCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resuleCount > 0){
            return ServerResponse.createByErrorMessage("该邮箱已被占用");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0 ){
            return ServerResponse.createBySuccess("更新成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("跟新失败");
    }
    //// TODO: 2017/7/15        getInformation
    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    //// TODO: 2017/7/15        checkAdminRole
    public ServerResponse<String> checkAdminRole(){
        return null;
    }
}
