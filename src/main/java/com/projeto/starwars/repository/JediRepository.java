package com.projeto.starwars.repository;
import com.projeto.starwars.model.Jedi;

import java.util.List;
import java.util.Optional;

public interface JediRepository {
    Optional<Jedi> findById(int id);

    List<Jedi> findAll();

    boolean update(Jedi jedi);

    Jedi save(Jedi jedi);

    boolean delete(int id);
}
