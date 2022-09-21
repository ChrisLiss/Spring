package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dao.UserDao;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.NotSavedException;
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

        log.info("user for create: {}", userDto);
        UserEntity userEntity = userMapper.userDtoToUserEntity(userDto);
        log.info("Mapped user: {}", userEntity);
        UserEntity userFromStorage = userDao.saveAndReturn(userEntity)
                .orElseThrow(() -> new NotSavedException(String.format("User with full name %s not saved", userDto.getFullName())));
        log.info("user from storage: {}", userFromStorage);
        UserDto userSaved = userMapper.userEntityToUserDto(userFromStorage);
        log.info("mapped user from storage: {}", userSaved);

        return userSaved;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        log.info("user for update, id user for update: {} {}", userDto, userId);
        UserEntity userEntity = userDao.getUserById(userId)             // проверим, есть ли user для обновления в хранилище
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));
        log.info("user from storage: {}", userEntity);
        userDto.setId(userId);
        userDao.update(userMapper.userDtoToUserEntity(userDto));
        UserDto userAfterUpdate = userMapper.userEntityToUserDto(userDao.getUserById(userId).orElse(null));
        log.info("user from storage after update: {}", userAfterUpdate);
        return userAfterUpdate;
    }

    @Override
    public UserDto getUserById(Long id) {
        UserEntity userEntity = userDao.getUserById(id).orElse(null);  // если не нашли по id - это не ошибка. Вернем null
        log.info("user from storage: {}", userEntity);
        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public void deleteUserById(Long id) {
        log.info("delete user with id: {}", id);
        userDao.delete(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("get all users:");
        return userDao.getAll().stream()
                .map(userMapper::userEntityToUserDto)
                .collect(Collectors.toList());
    }
}
