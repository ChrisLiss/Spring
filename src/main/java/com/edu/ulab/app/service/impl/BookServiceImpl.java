package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dao.BookDao;
import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.exception.NotSavedException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookDao bookDao;
    @Override
    public BookDto createBook(BookDto bookDto) {
        log.info("book for create: {}", bookDto);
        BookEntity bookEntity = bookMapper.bookDtoToBookEntity(bookDto);
        log.info("Mapped book: {}", bookEntity);
        BookEntity bookFromStorage = bookDao.saveAndReturn(bookEntity)
                .orElseThrow(() -> new NotSavedException("Book not saved"));
        log.info("book from storage: {}", bookFromStorage);
        BookDto bookSaved = bookMapper.bookEntityToBookDto(bookFromStorage);
        log.info("mapped book from storage: {}", bookSaved);
        return bookSaved;
    }
    @Override
    public List<Long> getListBookIdsByUserId(Long userId) {
        log.info("get all books for user id: {}", userId);
        return bookDao.getBookIdsByUserId(userId);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        return null;
    }

    @Override
    public BookDto getBookById(Long id) {
        return null;
    }

    @Override
    public void deleteBookById(Long id) {
        log.info("delete book with id: {}", id);
        bookDao.delete(id);
    }
}
