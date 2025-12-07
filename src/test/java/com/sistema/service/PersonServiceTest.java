package com.sistema.service;

import com.sistema.domain.Age;
import com.sistema.domain.Email;
import com.sistema.domain.Person;
import com.sistema.domain.PersonId;
import com.sistema.exception.DuplicatePersonException;
import com.sistema.exception.PersonNotFoundException;
import com.sistema.repository.InMemoryPersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    private PersonService personService;
    private Person testPerson;

    @BeforeEach
    void setUp() {
        var repository = new InMemoryPersonRepository();
        personService = new PersonService(repository);
        testPerson = new Person(
                new PersonId(1L),
                "João Silva",
                new Email("joao@email.com"),
                new Age(30));
    }

    @Test
    void shouldCreatePerson() {
        personService.createPerson(testPerson);

        var foundPerson = personService.findPersonById(testPerson.id());
        assertEquals(testPerson, foundPerson);
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicatePerson() {
        personService.createPerson(testPerson);

        assertThrows(DuplicatePersonException.class, () -> personService.createPerson(testPerson));
    }

    @Test
    void shouldFindPersonById() {
        personService.createPerson(testPerson);

        var foundPerson = personService.findPersonById(testPerson.id());
        assertEquals(testPerson, foundPerson);
    }

    @Test
    void shouldThrowExceptionWhenPersonNotFound() {
        assertThrows(PersonNotFoundException.class, () -> personService.findPersonById(new PersonId(999L)));
    }

    @Test
    void shouldUpdatePerson() {
        personService.createPerson(testPerson);

        var updatedPerson = testPerson.withName("José Silva");
        personService.updatePerson(updatedPerson);

        var foundPerson = personService.findPersonById(testPerson.id());
        assertEquals("José Silva", foundPerson.name());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentPerson() {
        assertThrows(PersonNotFoundException.class, () -> personService.updatePerson(testPerson));
    }

    @Test
    void shouldDeletePerson() {
        personService.createPerson(testPerson);

        personService.deletePersonById(testPerson.id());

        assertThrows(PersonNotFoundException.class, () -> personService.findPersonById(testPerson.id()));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPerson() {
        assertThrows(PersonNotFoundException.class, () -> personService.deletePersonById(new PersonId(999L)));
    }

    @Test
    void shouldFindAllPersons() {
        var person2 = new Person(
                new PersonId(2L),
                "Maria",
                new Email("maria@email.com"),
                new Age(25));

        personService.createPerson(testPerson);
        personService.createPerson(person2);

        var allPersons = personService.findAllPersons();
        assertEquals(2, allPersons.size());
        assertTrue(allPersons.contains(testPerson));
        assertTrue(allPersons.contains(person2));
    }

    @Test
    void shouldFailEarlyWhenCreatingNullPerson() {
        assertThrows(IllegalArgumentException.class, () -> personService.createPerson(null));
    }

    @Test
    void shouldFailEarlyWhenFindingWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> personService.findPersonById(null));
    }

    @Test
    void shouldFailEarlyWhenDeletingWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> personService.deletePersonById(null));
    }
}