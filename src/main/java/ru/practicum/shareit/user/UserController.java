package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserCreateDto dto) {
        log.info("Создание нового пользователя {}...", dto);
        UserDto newUser = userService.create(dto);
        log.info("Пользователь создан => {}.", newUser);

        return newUser;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto findById(@PathVariable @Positive long userId) {
        log.info("Поиск пользователя c id={}...", userId);
        UserDto foundUser = userService.findById(userId);
        log.info("Пользователь найден => {}.", foundUser);

        return foundUser;
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(
            @RequestBody @Valid UserUpdateDto dto,
            @PathVariable @Positive long userId
    ) {
        dto.setId(userId);
        log.info("Обновление данных {} пользователя с id={}...", dto, userId);
        UserDto updatedUser = userService.update(dto);
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
