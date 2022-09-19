package com.edu.ulab.app.dao;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.storage.BookStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BookDao {

    private final BookStorage bookStorage;

    public Optional<BookEntity> saveAndReturn(BookEntity book) {
        book.setId(bookStorage.getNewId());
        bookStorage.save(book);
        return bookStorage.getById(book.getId());
    }

    public void delete(Long id) {
        bookStorage.delete(id);
    }

    public List<Long> getBookIdsByUserId(Long userId) {
        return bookStorage.getBookIdsByUserId(userId);
    }

}
