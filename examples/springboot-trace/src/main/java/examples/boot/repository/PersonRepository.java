package examples.boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import examples.boot.entity.Person;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findOneById(Long id);
}
