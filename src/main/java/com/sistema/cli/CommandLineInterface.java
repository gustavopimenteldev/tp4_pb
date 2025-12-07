package com.sistema.cli;

import com.sistema.domain.Age;
import com.sistema.domain.Email;
import com.sistema.domain.Person;
import com.sistema.domain.PersonId;
import com.sistema.service.PersonService;

import java.util.Scanner;

public class CommandLineInterface {
    private final PersonService personService;
    private final Scanner scanner;
    private boolean running;
    
    public CommandLineInterface(PersonService personService) {
        this.personService = personService;
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    public void start() {
        System.out.println("=== Sistema CRUD de Pessoas ===");
        
        while (running) {
            showMenu();
            var option = readMenuOption();
            executeOption(option);
        }
        
        scanner.close();
    }
    
    private void showMenu() {
        System.out.println("\nOpções:");
        System.out.println("1. Criar pessoa");
        System.out.println("2. Buscar pessoa");
        System.out.println("3. Listar todas as pessoas");
        System.out.println("4. Atualizar pessoa");
        System.out.println("5. Deletar pessoa");
        System.out.println("6. Sair");
        System.out.print("Escolha uma opção: ");
    }
    
    private MenuOption readMenuOption() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            return switch (choice) {
                case 1 -> MenuOption.CREATE;
                case 2 -> MenuOption.READ;
                case 3 -> MenuOption.READ_ALL;
                case 4 -> MenuOption.UPDATE;
                case 5 -> MenuOption.DELETE;
                case 6 -> MenuOption.EXIT;
                default -> throw new IllegalArgumentException("Opção inválida");
            };
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Opção inválida. Tente novamente.");
            return readMenuOption();
        }
    }
    
    private void executeOption(MenuOption option) {
        try {
            switch (option) {
                case CREATE -> createPerson();
                case READ -> readPerson();
                case READ_ALL -> readAllPersons();
                case UPDATE -> updatePerson();
                case DELETE -> deletePerson();
                case EXIT -> {
                    running = false;
                    System.out.println("Saindo do sistema...");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    private void createPerson() {
        System.out.print("ID: ");
        long id = scanner.nextLong();
        scanner.nextLine();
        
        System.out.print("Nome: ");
        String name = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Idade: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        
        var person = new Person(
            new PersonId(id),
            name,
            new Email(email),
            new Age(age)
        );
        
        personService.createPerson(person);
        System.out.println("Pessoa criada com sucesso!");
    }
    
    private void readPerson() {
        System.out.print("Digite o ID: ");
        long id = scanner.nextLong();
        scanner.nextLine();
        
        var person = personService.findPersonById(new PersonId(id));
        printPerson(person);
    }
    
    private void readAllPersons() {
        var persons = personService.findAllPersons();
        if (persons.isEmpty()) {
            System.out.println("Nenhuma pessoa encontrada.");
        } else {
            persons.forEach(this::printPerson);
        }
    }
    
    private void updatePerson() {
        System.out.print("Digite o ID da pessoa a ser atualizada: ");
        long id = scanner.nextLong();
        scanner.nextLine();
        
        var personId = new PersonId(id);
        var existingPerson = personService.findPersonById(personId);
        
        System.out.print("Novo nome [" + existingPerson.name() + "]: ");
        String name = scanner.nextLine();
        if (name.trim().isEmpty()) {
            name = existingPerson.name();
        }
        
        System.out.print("Novo email [" + existingPerson.email().value() + "]: ");
        String email = scanner.nextLine();
        if (email.trim().isEmpty()) {
            email = existingPerson.email().value();
        }
        
        System.out.print("Nova idade [" + existingPerson.age().value() + "]: ");
        String ageInput = scanner.nextLine();
        int age = ageInput.trim().isEmpty() ? 
            existingPerson.age().value() : Integer.parseInt(ageInput);
        
        var updatedPerson = new Person(
            personId,
            name,
            new Email(email),
            new Age(age)
        );
        
        personService.updatePerson(updatedPerson);
        System.out.println("Pessoa atualizada com sucesso!");
    }
    
    private void deletePerson() {
        System.out.print("Digite o ID da pessoa a ser deletada: ");
        long id = scanner.nextLong();
        scanner.nextLine();
        
        personService.deletePersonById(new PersonId(id));
        System.out.println("Pessoa deletada com sucesso!");
    }
    
    private void printPerson(Person person) {
        System.out.println("ID: " + person.id().value());
        System.out.println("Nome: " + person.name());
        System.out.println("Email: " + person.email().value());
        System.out.println("Idade: " + person.age().value());
        System.out.println("---");
    }
}