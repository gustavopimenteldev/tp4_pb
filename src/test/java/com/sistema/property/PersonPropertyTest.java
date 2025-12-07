package com.sistema.property;

import com.sistema.domain.Age;
import com.sistema.domain.Email;
import com.sistema.domain.Person;
import com.sistema.domain.PersonId;
import com.sistema.repository.InMemoryPersonRepository;
import com.sistema.service.PersonService;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

class PersonPropertyTest {
    
    @Property
    void personCreatedCanAlwaysBeFound(@ForAll("validPersons") Person person) {
        var repository = new InMemoryPersonRepository();
        var service = new PersonService(repository);
        
        service.createPerson(person);
        var foundPerson = service.findPersonById(person.id());
        
        assert foundPerson.equals(person);
    }
    
    @Property
    void updatedPersonKeepsSameId(@ForAll("validPersons") Person person,
                                  @ForAll("validNames") String newName) {
        var repository = new InMemoryPersonRepository();
        var service = new PersonService(repository);
        
        service.createPerson(person);
        var updatedPerson = person.withName(newName);
        service.updatePerson(updatedPerson);
        
        var foundPerson = service.findPersonById(person.id());
        assert foundPerson.id().equals(person.id());
        assert foundPerson.name().equals(newName);
    }
    
    @Provide
    Arbitrary<Person> validPersons() {
        return Combinators.combine(
            validPersonIds(),
            validNames(),
            validEmails(),
            validAges()
        ).as(Person::new);
    }
    
    @Provide
    Arbitrary<PersonId> validPersonIds() {
        return Arbitraries.longs()
            .between(1L, Long.MAX_VALUE)
            .map(PersonId::new);
    }
    
    @Provide
    Arbitrary<String> validNames() {
        return Arbitraries.strings()
            .withCharRange('a', 'z')
            .withCharRange('A', 'Z')
            .withChars(' ')
            .ofMinLength(1)
            .ofMaxLength(50)
            .filter(s -> !s.trim().isEmpty());
    }
    
    @Provide
    Arbitrary<Email> validEmails() {
        var localPart = Arbitraries.strings()
            .withCharRange('a', 'z')
            .withCharRange('0', '9')
            .ofMinLength(1)
            .ofMaxLength(10);
            
        var domain = Arbitraries.strings()
            .withCharRange('a', 'z')
            .ofMinLength(2)
            .ofMaxLength(10);
            
        var tld = Arbitraries.of("com", "org", "net", "edu");
        
        return Combinators.combine(localPart, domain, tld)
            .as((local, dom, t) -> local + "@" + dom + "." + t)
            .map(Email::new);
    }
    
    @Provide
    Arbitrary<Age> validAges() {
        return Arbitraries.integers()
            .between(0, 150)
            .map(Age::new);
    }
}