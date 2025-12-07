package com.sistema.util;

import com.sistema.domain.Age;
import com.sistema.domain.Email;
import com.sistema.domain.Person;
import com.sistema.domain.PersonId;
import com.sistema.web.dto.PersonDto;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    
    public PersonDto toDto(Person person) {
        return new PersonDto(
            person.id().value(),
            person.name(),
            person.email().value(),
            person.age().value()
        );
    }
    
    public Person toDomain(PersonDto dto) {
        return new Person(
            new PersonId(dto.getId()),
            dto.getName(),
            new Email(dto.getEmail()),
            new Age(dto.getAge())
        );
    }
}