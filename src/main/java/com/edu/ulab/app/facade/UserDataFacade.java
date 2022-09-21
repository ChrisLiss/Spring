package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.ListUserBookResponses;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = createBooksAndReturnIds(userBookRequest, createdUser.getId());

        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    private List<Long> createBooksAndReturnIds(UserBookRequest userBookRequest, Long userId) {
        log.info("create books for userId: {}", userId);
        return  userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(userId))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
    }

    private void deleteAllBooksByUserId(Long userId) {
        List<Long> bookIdListFromStorage = bookService.getListBookIdsByUserId(userId);
        log.info("delete all books for userId: {}", userId);
        bookIdListFromStorage.stream()
                .peek(bookId -> log.info("delete book with id: {}", bookId))
                .forEach(bookService::deleteBookById);
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest, Long userId) {

        // Будем считать, что необходимо полностью обновить список книг для юзера (т. к. id книг не паредаются)

        log.info("Update user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        userDto.setId(userId);
        log.info("Mapped user request: {}", userDto);
        UserDto userDtoAfterUpdate = userService.updateUser(userDto, userId);
        deleteAllBooksByUserId(userId);
        List<Long> bookIdList = createBooksAndReturnIds(userBookRequest, userId);
        log.info("Collected new book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(userDtoAfterUpdate.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {

        log.info("Search user with idUser: {}", userId);
        UserDto userDto = userService.getUserById(userId);
        log.info("Found user with idUser: {}  {}", userId, userDto);
        List<Long> bookIdList = bookService.getListBookIdsByUserId(userId);
        bookIdList.forEach(book ->log.info("Found books for user with idUser: {}  {}", userId, book));

        if (userDto == null) {
            log.info("user with idUser: {} не найден", userId);
            return null;
        }

        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public void deleteUserWithBooks(Long userId) {

        deleteAllBooksByUserId(userId);
        log.info("delete user with idUser: {}", userId);
        userService.deleteUserById(userId);
    }

    public ListUserBookResponses getAllUserWithBooks() {

        log.info("Search all user with books");
        List<UserBookResponse> allUsersWithBooks =  userService.getAllUsers().stream()
                .filter(Objects::nonNull)
                .map(user -> UserBookResponse.builder()
                                   .userId(user.getId())
                                   .booksIdList(bookService.getListBookIdsByUserId(user.getId()))
                                   .build())
                .collect(Collectors.toList());

        log.info("All users with book: {}", allUsersWithBooks);
        return new ListUserBookResponses(allUsersWithBooks);
    }
}
