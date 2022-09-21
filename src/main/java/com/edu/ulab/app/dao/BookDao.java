package com.edu.ulab.app.dao;

import com.edu.ulab.app.entity.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Optional<BookEntity> saveAndReturn(BookEntity book);

    void delete(Long id);

    List<Long> getBookIdsByUserId(Long userId);
}
