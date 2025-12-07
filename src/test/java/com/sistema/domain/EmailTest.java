package com.sistema.domain;

import com.sistema.exception.InvalidEmailException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {
    
    @Test
    void shouldCreateValidEmail() {
        var email = new Email("test@example.com");
        assertEquals("test@example.com", email.value());
    }
    
    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(InvalidEmailException.class, () -> 
            new Email("invalid-email")
        );
    }
    
    @Test
    void shouldThrowExceptionForNullEmail() {
        assertThrows(InvalidEmailException.class, () -> 
            new Email(null)
        );
    }
    
    @Test
    void shouldThrowExceptionForEmptyEmail() {
        assertThrows(InvalidEmailException.class, () -> 
            new Email("")
        );
    }
}