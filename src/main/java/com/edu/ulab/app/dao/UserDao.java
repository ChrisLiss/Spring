package com.edu.ulab.app.dao;

import com.edu.ulab.app.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<UserEntity> getUserById(Long userId);

    Optional<UserEntity> saveAndReturn(UserEntity user);

    void delete(Long userId);

    List<UserEntity> getAll();

    void update(UserEntity userEntity);
}
