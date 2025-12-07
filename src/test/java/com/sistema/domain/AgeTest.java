package com.sistema.domain;

import com.sistema.exception.InvalidAgeException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AgeTest {
    
    @Test
    void shouldCreateValidAge() {
        var age = new Age(25);
        assertEquals(25, age.value());
    }
    
    @Test
    void shouldThrowExceptionForNegativeAge() {
        assertThrows(InvalidAgeException.class, () -> 
            new Age(-1)
        );
    }
    
    @Test
    void shouldThrowExceptionForTooOldAge() {
        assertThrows(InvalidAgeException.class, () -> 
            new Age(151)
        );
    }
    
    @Test
    void shouldAcceptBoundaryAges() {
        assertDoesNotThrow(() -> new Age(0));
        assertDoesNotThrow(() -> new Age(150));
    }
}