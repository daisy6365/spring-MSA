package com.study.service;

import com.study.domain.UserEntity;
import com.study.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto userDto);

    // 한명의 데이터 조회
    UserDto getUserByUserId(String userId);

    // 전체 데이터 조회
    Iterable<UserEntity> getUserByAll();
}
