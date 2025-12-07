package com.sistema.repository;

import com.sistema.domain.Person;
import com.sistema.domain.PersonId;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    void save(Person person);
    Optional<Person> findById(PersonId id);
    List<Person> findAll();
    void deleteById(PersonId id);
    boolean existsById(PersonId id);
}