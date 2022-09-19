package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class UserStorage {

    // TODO: 18.09.2022 Генерация id 

    private final Map<Long, UserEntity> userMap;

    public UserStorage() {
        this.userMap = new ConcurrentHashMap<>();
    }

    public Long getNewId(){
        Long maxId = userMap.keySet().stream()
                .max(Long::compare)
                .orElse(0L);
        return maxId + 1;
    }
    public void save(UserEntity userEntity) {
        userMap.put(userEntity.getId(), userEntity);
    }

    public Optional<UserEntity> getById(Long userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    public void delete(Long userId) {
        userMap.remove(userId);
    }

    public List<UserEntity> getAll() {
        return new ArrayList<>(userMap.values());
    }
}
