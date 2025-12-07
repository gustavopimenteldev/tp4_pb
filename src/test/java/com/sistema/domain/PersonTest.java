package com.sistema.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    void shouldCreateValidPerson() {
        var person = new Person(
                new PersonId(1L),
                "João Silva",
                new Email("joao@email.com"),
                new Age(30));

        assertEquals(1L, person.id().value());
        assertEquals("João Silva", person.name());
        assertEquals("joao@email.com", person.email().value());
        assertEquals(30, person.age().value());
    }

    @Test
    void shouldThrowExceptionForNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Person(
                new PersonId(1L),
                null,
                new Email("joao@email.com"),
                new Age(30)));
    }

    @Test
    void shouldThrowExceptionForEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Person(
                new PersonId(1L),
                "   ",
                new Email("joao@email.com"),
                new Age(30)));
    }

    @Test
    void shouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> new Person(
                null,
                "João Silva",
                new Email("joao@email.com"),
                new Age(30)));
    }

    @Test
    void shouldThrowExceptionForNullEmail() {
        assertThrows(IllegalArgumentException.class, () -> new Person(
                new PersonId(1L),
                "João Silva",
                null,
                new Age(30)));
    }

    @Test
    void shouldThrowExceptionForNullAge() {
        assertThrows(IllegalArgumentException.class, () -> new Person(
                new PersonId(1L),
                "João Silva",
                new Email("joao@email.com"),
                null));
    }

    @Test
    void shouldCreatePersonWithNewName() {
        var originalPerson = new Person(
                new PersonId(1L),
                "João Silva",
                new Email("joao@email.com"),
                new Age(30));

        var updatedPerson = originalPerson.withName("José Silva");

        assertEquals("José Silva", updatedPerson.name());
        assertEquals(originalPerson.id(), updatedPerson.id());
        assertEquals(originalPerson.email(), updatedPerson.email());
        assertEquals(originalPerson.age(), updatedPerson.age());
    }

    @Test
    void shouldCreatePersonWithNewEmail() {
        var originalPerson = new Person(
                new PersonId(1L),
                "João Silva",
                new Email("joao@email.com"),
                new Age(30));

        var newEmail = new Email("novo@email.com");
        var updatedPerson = originalPerson.withEmail(newEmail);

        assertEquals(newEmail, updatedPerson.email());
        assertEquals(originalPerson.id(), updatedPerson.id());
        assertEquals(originalPerson.name(), updatedPerson.name());
        assertEquals(originalPerson.age(), updatedPerson.age());
    }

    @Test
    void shouldCreatePersonWithNewAge() {
        var originalPerson = new Person(
                new PersonId(1L),
                "João Silva",
                new Email("joao@email.com"),
                new Age(30));

        var newAge = new Age(35);
        var updatedPerson = originalPerson.withAge(newAge);

        assertEquals(newAge, updatedPerson.age());
        assertEquals(originalPerson.id(), updatedPerson.id());
        assertEquals(originalPerson.name(), updatedPerson.name());
        assertEquals(originalPerson.email(), updatedPerson.email());
    }
}