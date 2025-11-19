package ru.yandex.practicum.filmorate.intf;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CrudInterface<E> {

    ResponseEntity<E> create(E e);

    E update(long id, E e);

    List<E> getAll();

     E read(Long id);

}
