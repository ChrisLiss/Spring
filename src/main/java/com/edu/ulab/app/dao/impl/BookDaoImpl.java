package com.edu.ulab.app.dao.impl;

import com.edu.ulab.app.dao.BookDao;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.storage.BookStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BookDaoImpl implements BookDao {

    private final BookStorage bookStorage;

    @Override
    public Optional<BookEntity> saveAndReturn(BookEntity book) {
        book.setId(bookStorage.getNewId());
        bookStorage.save(book);
        return bookStorage.getById(book.getId());
    }

    @Override
    public void delete(Long id) {
        bookStorage.delete(id);
    }

    @Override
    public List<Long> getBookIdsByUserId(Long userId) {
        return bookStorage.getBookIdsByUserId(userId);
    }

}
