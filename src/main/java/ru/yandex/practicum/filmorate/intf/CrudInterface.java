package ru.yandex.practicum.filmorate.intf;

import java.util.List;

public interface CrudInterface<E> {

    abstract E create (E e);
    abstract E update (long id, E e);
    abstract List<E> getAll();
    abstract E read(Long id);
}
