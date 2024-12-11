package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.request.create.UserRequestCreateDto;
import ru.practicum.shareit.user.dto.request.update.UserRequestUpdateDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@RequestBody @Valid UserRequestCreateDto userRequestCreateDto) {
        log.info("Создание нового пользователя {}...", userRequestCreateDto);
        UserResponseDto newUser = userService.create(userRequestCreateDto);
        log.info("Пользователь создан => {}.", newUser);

        return newUser;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto findById(@PathVariable @Positive long userId) {
        log.info("Поиск пользователя c id={}...", userId);
        UserResponseDto foundUser = userService.findById(userId);
        log.info("Пользователь найден => {}.", foundUser);

        return foundUser;
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto update(
            @RequestBody @Valid UserRequestUpdateDto userRequestUpdateDto,
            @PathVariable @Positive long userId
    ) {
        userRequestUpdateDto.setId(userId);
        log.info("Обновление данных {} пользователя с id={}...", userRequestUpdateDto, userId);
        UserResponseDto updatedUser = userService.update(userRequestUpdateDto);
        log.info("Пользователь обновлен => {}.", updatedUser);

        return updatedUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive long userId) {
        log.info("Удаление пользователя с id={}...", userId);
        userService.delete(userId);
        log.info("Пользователь с id={} удален.", userId);
    }
}
