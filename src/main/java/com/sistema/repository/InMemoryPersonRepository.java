package com.sistema.repository;

import com.sistema.domain.Person;
import com.sistema.domain.PersonId;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPersonRepository implements PersonRepository {
    private final Map<PersonId, Person> storage = new ConcurrentHashMap<>();
    
    @Override
    public void save(Person person) {
        storage.put(person.id(), person);
    }
    
    @Override
    public Optional<Person> findById(PersonId id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<Person> findAll() {
        return List.copyOf(storage.values());
    }
    
    @Override
    public void deleteById(PersonId id) {
        storage.remove(id);
    }
    
    @Override
    public boolean existsById(PersonId id) {
        return storage.containsKey(id);
    }
}