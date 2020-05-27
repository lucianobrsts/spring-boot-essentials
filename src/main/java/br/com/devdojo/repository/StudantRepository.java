package br.com.devdojo.repository;

import br.com.devdojo.model.Studant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StudantRepository extends PagingAndSortingRepository<Studant, Long> {
    List<Studant> findByNameIgnoreCaseContaining(String name);
}
