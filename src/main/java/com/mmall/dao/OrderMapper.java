package com.mmall.dao;

import com.mmall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByOrderNoAndUserId(@Param("userId") Integer userId, @Param("orderNo")Long orderNo);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectByUserId(Integer userId);


    List<Order> selectAllOrder();

}