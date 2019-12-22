package examples.boot.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import examples.boot.entity.Person;
import examples.boot.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/{id}")
    public ResponseEntity getPerson(@PathVariable("id") Long id) {
        Optional<Person> personOptional = personService.getPersonById(id);

        if (!personOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(personOptional.get());
    }

    @GetMapping
    public ResponseEntity getPersons() {
        return ResponseEntity.ok(personService.getPersons());
    }

    @PostMapping
    public ResponseEntity savePerson(@RequestBody Person person) {
        Map<String, Object> result = new HashMap<>();

        result.put("id", personService.savePerson(person));

        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/update/{id}")
    public ResponseEntity updatePerson(@PathVariable("id") Long id, @RequestBody Person person) {
        if (!personService.updatePerson(id, person)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deletePerson(@PathVariable("id") Long id) {
        if (!personService.deletePerson(id)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}