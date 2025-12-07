package com.sistema.property;

import com.sistema.domain.Age;
import com.sistema.domain.Email;
import com.sistema.domain.Person;
import com.sistema.domain.PersonId;
import com.sistema.exception.DuplicatePersonException;
import com.sistema.exception.InvalidAgeException;
import com.sistema.exception.InvalidEmailException;
import com.sistema.repository.InMemoryPersonRepository;
import com.sistema.service.PersonService;
import net.jqwik.api.*;

import static org.junit.jupiter.api.Assertions.fail;

class PersonFuzzTest {

    @Property
    void ageHandlesArbitraryIntegersWithoutUnexpectedExceptions(@ForAll int value) {
        try {
            new Age(value);
        } catch (InvalidAgeException e) {
        } catch (Exception e) {
            fail("Unexpected exception for age = " + value + ": " + e);
        }
    }

    @Property
    void emailHandlesArbitraryStringsWithoutUnexpectedExceptions(@ForAll("anyAsciiString") String candidate) {
        try {
            new Email(candidate);
        } catch (InvalidEmailException e) {
        } catch (Exception e) {
            fail("Unexpected exception for email = '" + candidate + "': " + e);
        }
    }

    @Provide
    Arbitrary<String> anyAsciiString() {
        return Arbitraries.strings()
                .ofMinLength(0)
                .ofMaxLength(256)
                .ascii(); 
    }

    @Property
    void serviceCreatePersonWithRandomDataDoesNotCrashUnexpectedly(
            @ForAll("namesWithWeirdChars") String name,
            @ForAll("anyAsciiString") String emailText,
            @ForAll int ageValue
    ) {
        var repository = new InMemoryPersonRepository();
        var service = new PersonService(repository);
        var id = new PersonId(1L); 
        try {
            var email = new Email(emailText);   
            var age = new Age(ageValue);     
            var person = new Person(id, name, email, age); 

            service.createPerson(person); 

        } catch (InvalidEmailException | InvalidAgeException | IllegalArgumentException | DuplicatePersonException e) {
        } catch (Exception e) {
            fail("Unexpected exception for fuzzed input (name='" + name +
                    "', email='" + emailText + "', age=" + ageValue + "): " + e);
        }
    }

    @Provide
    Arbitrary<String> namesWithWeirdChars() {
        return Arbitraries.strings()
                .ofMinLength(0)
                .ofMaxLength(100)
                .ascii();
    }
}
