package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dao.UserDao;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserDao userDao;
    @Override
    public UserDto createUser(UserDto userDto) {
        // сгенерировать идентификатор
        // создать пользователя
        // вернуть сохраненного пользователя со всеми необходимыми полями id

        UserEntity userEntity = userMapper.userDtoToUserEntity(userDto);
        UserEntity userFromStorage = userDao.saveAndReturn(userEntity)
                .orElseThrow(() -> new NotFoundException("User not saved"));
        UserDto userSaved = userMapper.userEntityToUserDto(userFromStorage);

        return userSaved;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        UserEntity userEntity = userDao.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        userDao.update(userEntity, userDto);
        UserDto userAfterUpdate = userMapper.userEntityToUserDto(userDao.getUserById(userEntity.getId()).orElse(null));
        return userAfterUpdate;
    }

    @Override
    public UserDto getUserById(Long id) {
        UserEntity userEntity = userDao.getUserById(id).orElse(null);  // если не нашли по id - это не ошибка. Вернем null
        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public void deleteUserById(Long id) {
        userDao.delete(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userDao.getAll().stream()
                .map(userMapper::userEntityToUserDto)
                .collect(Collectors.toList());
    }
}
