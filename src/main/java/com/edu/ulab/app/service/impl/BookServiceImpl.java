package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dao.BookDao;
import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.exception.NotFoundException;
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
        BookEntity bookEntity = bookMapper.bookDtoToBookEntity(bookDto);
        BookEntity bookFromStorage = bookDao.saveAndReturn(bookEntity)
                .orElseThrow(() -> new NotFoundException("Book not saved"));
        BookDto bookSaved = bookMapper.bookEntityToBookDto(bookFromStorage);
        return bookSaved;
    }
    @Override
    public List<Long> getListBookIdsByUserId(Long userId) {
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
        bookDao.delete(id);
    }
}
