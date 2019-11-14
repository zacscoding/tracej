package examples.boot.service;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import examples.boot.entity.Person;
import examples.boot.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Simple person crud service
 *
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    @PostConstruct
    private void setUp() {
        saveDummy();
    }

    public Optional<Person> getPersonById(Long id) {
        requireNonNull(id, "id");
        return personRepository.findOneById(id);
    }

    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    public Long savePerson(Person person) {
        requireNonNull(person);
        person = personRepository.save(person);
        return person.getId();
    }

    public boolean updatePerson(Long id, Person person) {
        Optional<Person> personOptional = personRepository.findOneById(id);
        if (!personOptional.isPresent()) {
            return false;
        }

        Person saved = personOptional.get();

        saved.setAge(person.getAge());
        saved.setName(person.getName());
        saved.setHobbies(person.getHobbies());

        personRepository.save(saved);
        return true;
    }

    public boolean deletePerson(Long id) {
        if (!personRepository.findById(requireNonNull(id, "id")).isPresent()) {
            return false;
        }
        personRepository.deleteById(id);
        return true;
    }

    private void saveDummy() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Person person = Person.builder()
                                  .name("Hiva" + i)
                                  .age(i * 10 + 5)
                                  .hobbies(randomHobies())
                                  .build();

            Person savedPerson = personRepository.save(person);
            log.info("[Created dummy person] " + savedPerson);
        });
    }

    private List<String> randomHobies() {
        String[] defaultHobies = { "coding", "book", "movie", "math" };

        List<String> hobbies = new ArrayList<>(4);
        Random random = new Random();

        for (String defaultHobby : defaultHobies) {
            if (random.nextInt() % 4 == 0) {
                hobbies.add(defaultHobby);
            }
        }

        return hobbies;
    }

}
