package ru.practicum.shareit;

public interface BaseService<CreateDto, UpdateDto, ResponseDto> {
    ResponseDto create(CreateDto dto);

    ResponseDto findById(long id);

    ResponseDto update(UpdateDto dto);

    void delete(long id);
}
