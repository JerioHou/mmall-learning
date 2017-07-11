package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/10.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候,如果是null的对象,key也会消失
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, T data) {

        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status) {

        this.status = status;
    }

    @JsonIgnore
    //使之不在json序列化结果当中
    public  boolean isSuccess(){
        return this.status == ResponeCode.SUCCESS.getCode();
    }


    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponeCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponeCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponeCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponeCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponeCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponeCode.ERROR.getCode(),ResponeCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByError(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }
}