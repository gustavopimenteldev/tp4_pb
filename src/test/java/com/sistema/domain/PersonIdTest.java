package com.sistema.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonIdTest {

    @Test
    void shouldCreateValidPersonId() {
        var id = new PersonId(1L);
        assertEquals(1L, id.value());
    }

    @Test
    void shouldThrowExceptionForZeroId() {
        assertThrows(IllegalArgumentException.class, () -> new PersonId(0L));
    }

    @Test
    void shouldThrowExceptionForNegativeId() {
        assertThrows(IllegalArgumentException.class, () -> new PersonId(-1L));
    }

    @Test
    void shouldAcceptMaxLongId() {
        assertDoesNotThrow(() -> new PersonId(Long.MAX_VALUE));
    }
}
