package com.sistema.domain;

public record Person(
    PersonId id,
    String name,
    Email email,
    Age age
) {
    public Person {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (id == null) {
            throw new IllegalArgumentException("PersonId cannot be null");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (age == null) {
            throw new IllegalArgumentException("Age cannot be null");
        }
    }
    
    public Person withName(String newName) {
        return new Person(id, newName, email, age);
    }
    
    public Person withEmail(Email newEmail) {
        return new Person(id, name, newEmail, age);
    }
    
    public Person withAge(Age newAge) {
        return new Person(id, name, email, newAge);
    }
}