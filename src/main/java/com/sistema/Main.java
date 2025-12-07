package com.sistema;

import com.sistema.cli.CommandLineInterface;
import com.sistema.repository.InMemoryPersonRepository;
import com.sistema.service.PersonService;

public class Main {
    public static void main(String[] args) {
        var repository = new InMemoryPersonRepository();
        var service = new PersonService(repository);
        var cli = new CommandLineInterface(service);
        
        cli.start();
    }
}