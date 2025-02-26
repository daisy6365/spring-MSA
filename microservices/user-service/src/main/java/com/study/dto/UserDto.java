package com.study.dto;

import com.study.response.ResponseOrder;
import lombok.Data;

import java.util.Date;
import java.util.List;

// TODO: 2025-02-26 강의용으로 추가했으나, 삭제 예정
@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createAt;
    private String encryptedPwd;

    private List<ResponseOrder> orderList;
}
