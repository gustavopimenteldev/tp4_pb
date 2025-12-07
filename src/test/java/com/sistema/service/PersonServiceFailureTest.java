package com.sistema.service;

import com.sistema.domain.Age;
import com.sistema.domain.Email;
import com.sistema.domain.Person;
import com.sistema.domain.PersonId;
import com.sistema.exception.SystemFailureException;
import com.sistema.repository.PersonRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonServiceFailureTest {

    @Test
    void shouldWrapNetworkFailureAsSystemFailureExceptionOnFindAll() {
        var service = new PersonService(new FailingPersonRepository(FailureMode.NETWORK));

        var ex = assertThrows(SystemFailureException.class, service::findAllPersons);
        assertTrue(ex.getMessage().contains("Failed to find all persons"));
    }

    @Test
    void shouldWrapTimeoutFailureAsSystemFailureExceptionOnCreate() {
        var service = new PersonService(new FailingPersonRepository(FailureMode.TIMEOUT));
        var person = new Person(
                new PersonId(1L),
                "Test User",
                new Email("test@email.com"),
                new Age(30));

        var ex = assertThrows(SystemFailureException.class, () -> service.createPerson(person));
        assertTrue(ex.getMessage().contains("Failed to create person"));
    }

    @Test
    void shouldWrapOverloadFailureAsSystemFailureExceptionOnDelete() {
        var service = new PersonService(new FailingPersonRepository(FailureMode.OVERLOAD));
        var id = new PersonId(99L);

        var ex = assertThrows(SystemFailureException.class, () -> service.deletePersonById(id));
        assertTrue(ex.getMessage().contains("Failed to delete person"));
    }

    private enum FailureMode {
        NETWORK,
        TIMEOUT,
        OVERLOAD
    }

    private static class FailingPersonRepository implements PersonRepository {

        private final FailureMode mode;

        FailingPersonRepository(FailureMode mode) {
            this.mode = mode;
        }

        private RuntimeException failure() {
            return switch (mode) {
                case NETWORK -> new RuntimeException("Simulated network failure");
                case TIMEOUT -> new RuntimeException("Simulated timeout");
                case OVERLOAD -> new RuntimeException("Simulated overload");
            };
        }

        @Override
        public void save(Person person) {
            throw failure();
        }

        @Override
        public Optional<Person> findById(PersonId id) {
            throw failure();
        }

        @Override
        public List<Person> findAll() {
            throw failure();
        }

        @Override
        public void deleteById(PersonId id) {
            throw failure();
        }

        @Override
        public boolean existsById(PersonId id) {
            throw failure();
        }
    }
}
