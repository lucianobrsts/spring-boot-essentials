package br.com.devdojo.repository;

import br.com.devdojo.model.Studant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudantRepository extends CrudRepository<Studant, Long> {
    List<Studant> findByName(String name);
}
