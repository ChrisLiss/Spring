package com.edu.ulab.app.dao;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserDao {

    private final UserStorage userStorage;

    public Optional<UserEntity> getUserById(Long userId) {
        return userStorage.getById(userId);
    }

    public Optional<UserEntity> saveAndReturn(UserEntity user) {
        user.setId(userStorage.getNewId());
        userStorage.save(user);
        return userStorage.getById(user.getId());
    }

    public void delete(Long userId) {
        userStorage.delete(userId);
    }

    public List<UserEntity> getAll() {
        return userStorage.getAll();
    }

    public void update(UserEntity userEntity, UserDto userDto) {
       userEntity.setFullName(userDto.getFullName());
       userEntity.setTitle(userDto.getTitle());
       userEntity.setAge(userDto.getAge());

       userStorage.save(userEntity);
    }

}
