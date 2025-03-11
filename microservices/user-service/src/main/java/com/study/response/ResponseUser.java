package com.study.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
// TODO: 2025-02-26 강의용으로 추가했으나, 삭제 예정
@Data
// 데이터가 없으면 null -> 불필요한 데이터를 제어하기 위해 없으면 제외하는 기능
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {
    private String email;
    private String name;
    private String userId;

    // 사용자가 주문했던 주문내역 list
    private List<ResponseOrder> orderList;
}
