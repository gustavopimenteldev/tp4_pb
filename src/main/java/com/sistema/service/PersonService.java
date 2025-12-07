package com.sistema.service;

import com.sistema.domain.Person;
import com.sistema.domain.PersonId;
import com.sistema.exception.DuplicatePersonException;
import com.sistema.exception.PersonNotFoundException;
import com.sistema.exception.SystemFailureException;
import com.sistema.repository.PersonRepository;

import java.util.List;
import java.util.function.Supplier;

public class PersonService {
    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public void createPerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person must not be null");
        }

        executeVoid(() -> {
            if (repository.existsById(person.id())) {
                throw new DuplicatePersonException(
                    "Person with ID " + person.id().value() + " already exists"
                );
            }
            repository.save(person);
        }, "create person");
    }

    public Person findPersonById(PersonId id) {
        if (id == null) {
            throw new IllegalArgumentException("PersonId must not be null");
        }

        return execute(() ->
            repository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(
                    "Person with ID " + id.value() + " not found"
                )),
            "find person by id"
        );
    }

    public List<Person> findAllPersons() {
        return execute(repository::findAll, "find all persons");
    }

    public void updatePerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person must not be null");
        }

        executeVoid(() -> {
            if (!repository.existsById(person.id())) {
                throw new PersonNotFoundException(
                    "Person with ID " + person.id().value() + " not found"
                );
            }
            repository.save(person);
        }, "update person");
    }

    public void deletePersonById(PersonId id) {
        if (id == null) {
            throw new IllegalArgumentException("PersonId must not be null");
        }

        executeVoid(() -> {
            if (!repository.existsById(id)) {
                throw new PersonNotFoundException(
                    "Person with ID " + id.value() + " not found"
                );
            }
            repository.deleteById(id);
        }, "delete person");
    }

    private void executeVoid(Runnable action, String operationDescription) {
        execute(() -> {
            action.run();
            return null;
        }, operationDescription);
    }

    private <T> T execute(Supplier<T> action, String operationDescription) {
        try {
            return action.get();
        } catch (DuplicatePersonException | PersonNotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new SystemFailureException(
                "Failed to " + operationDescription + " due to infrastructure problem",
                e
            );
        }
    }
}
