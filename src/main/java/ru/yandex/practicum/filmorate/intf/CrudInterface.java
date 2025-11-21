package ru.yandex.practicum.filmorate.intf;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CrudInterface<E> {

    ResponseEntity<E> create(E e);

    ResponseEntity<E> update(long id, E e);

    ResponseEntity<E> update(E e);

    ResponseEntity<List<E>> getAll();

    ResponseEntity<E> read(Long id);

}
