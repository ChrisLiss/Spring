package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.BookEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class BookStorage {

    private final Map<Long, BookEntity> bookMap;

    public BookStorage() {
        this.bookMap = new ConcurrentHashMap<>();
    }

    public Long getNewId(){
        Long maxId = bookMap.keySet().stream()
                .max(Long::compare)
                .orElse(0L);
        return maxId + 1;
    }
    public void save(BookEntity bookEntity) {
        bookMap.put(bookEntity.getId(), bookEntity);
    }

    public Optional<BookEntity> getById(Long bookId) {
        return Optional.ofNullable(bookMap.get(bookId));
    }

    public void delete(Long bookId) {
        bookMap.remove(bookId);
    }

    public List<Long> getBookIdsByUserId(Long userId) {
        return bookMap.values().stream()
                .filter(book -> userId.equals(book.getUserId()))
                .map(BookEntity::getId)
                .collect(Collectors.toList());
    }

}
