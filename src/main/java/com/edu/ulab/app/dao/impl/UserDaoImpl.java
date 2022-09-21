package com.edu.ulab.app.dao.impl;

import com.edu.ulab.app.dao.UserDao;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserDaoImpl implements UserDao {

    private final UserStorage userStorage;

    @Override
    public Optional<UserEntity> getUserById(Long userId) {
        return userStorage.getById(userId);
    }

    @Override
    public Optional<UserEntity> saveAndReturn(UserEntity user) {
        user.setId(userStorage.getNewId());
        userStorage.save(user);
        return userStorage.getById(user.getId());
    }

    @Override
    public void delete(Long userId) {
        userStorage.delete(userId);
    }

    @Override
    public List<UserEntity> getAll() {
        return userStorage.getAll();
    }

    @Override
    public void update(UserEntity userEntity) {
       userStorage.save(userEntity);
    }

}
