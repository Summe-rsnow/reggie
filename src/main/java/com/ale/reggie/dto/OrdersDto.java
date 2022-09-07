package com.ale.reggie.dto;

import com.ale.reggie.domain.OrderDetail;
import com.ale.reggie.domain.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
