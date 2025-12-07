package com.sistema.web.config;

import com.sistema.repository.InMemoryPersonRepository;
import com.sistema.repository.PersonRepository;
import com.sistema.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    
    @Bean
    public PersonRepository personRepository() {
        return new InMemoryPersonRepository();
    }
    
    @Bean
    public PersonService personService(PersonRepository personRepository) {
        return new PersonService(personRepository);
    }
}