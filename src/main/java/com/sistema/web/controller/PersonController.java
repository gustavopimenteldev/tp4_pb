package com.sistema.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sistema.domain.PersonId;
import com.sistema.exception.DuplicatePersonException;
import com.sistema.exception.PersonNotFoundException;
import com.sistema.exception.SystemFailureException;
import com.sistema.service.PersonService;
import com.sistema.util.PersonMapper;
import com.sistema.web.dto.PersonDto;

import jakarta.validation.Valid;

@Controller
public class PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    public PersonController(PersonService personService, PersonMapper personMapper) {
        this.personService = personService;
        this.personMapper = personMapper;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/persons")
    public String listPersons(Model model) {
        var persons = personService.findAllPersons();
        var personDtos = persons.stream()
                .map(personMapper::toDto)
                .toList();
        model.addAttribute("persons", personDtos);
        return "person-list";
    }

    @GetMapping("/persons/new")
    public String showCreateForm(Model model) {
        model.addAttribute("person", new PersonDto());
        model.addAttribute("isEdit", false);
        return "person-form";
    }

    @GetMapping("/persons/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var person = personService.findPersonById(new PersonId(id));
            var personDto = personMapper.toDto(person);
            model.addAttribute("person", personDto);
            model.addAttribute("isEdit", true);
            return "person-form";
        } catch (PersonNotFoundException e) {
            redirectAttributes.addAttribute("error", "Person not found");
            return "redirect:/persons";
        } catch (SystemFailureException e) {
            redirectAttributes.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "redirect:/persons";
        }
    }

    @PostMapping("/persons")
    public String createPerson(
            @Valid @ModelAttribute("person") PersonDto personDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "person-form";
        }

        try {
            var person = personMapper.toDomain(personDto);
            personService.createPerson(person);
            redirectAttributes.addAttribute("success", "Person created successfully");
            return "redirect:/persons";
        } catch (DuplicatePersonException e) {
            redirectAttributes.addAttribute("error", "Person with this ID already exists");
            return "redirect:/persons";
        } catch (SystemFailureException e) {
            redirectAttributes.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "redirect:/persons";
        }
    }

    @PostMapping("/persons/update")
    public String updatePerson(
            @Valid @ModelAttribute("person") PersonDto personDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "person-form";
        }

        try {
            var person = personMapper.toDomain(personDto);
            personService.updatePerson(person);

            redirectAttributes.addAttribute("success", "Person updated successfully");
            return "redirect:/persons";
        } catch (PersonNotFoundException e) {
            redirectAttributes.addAttribute("error", "Person not found");
            return "redirect:/persons";
        } catch (SystemFailureException e) {
            redirectAttributes.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "redirect:/persons";
        }
    }

    @PostMapping("/persons/delete/{id}")
    public String deletePerson(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            personService.deletePersonById(new PersonId(id));
            redirectAttributes.addAttribute("success", "Person deleted successfully");
        } catch (PersonNotFoundException e) {
            redirectAttributes.addAttribute("error", "Person not found");
        } catch (SystemFailureException e) {
            redirectAttributes.addAttribute("error", "An unexpected error occurred. Please try again later.");
        }
        return "redirect:/persons";
    }
}
