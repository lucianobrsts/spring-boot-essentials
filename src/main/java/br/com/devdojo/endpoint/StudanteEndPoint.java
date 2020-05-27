package br.com.devdojo.endpoint;

import br.com.devdojo.error.CustomErrorType;
import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Studant;
import br.com.devdojo.repository.StudantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("studants")
public class StudanteEndPoint {
    private final StudantRepository studantDAO;

    //Faz parte do framework de injeção de dependencias do spring, ele faz a injeção das dependências e instancia o objeto para nós.
    @Autowired
    public StudanteEndPoint(StudantRepository studantDAO) {
        this.studantDAO = studantDAO;
    }

    @GetMapping
    public ResponseEntity<?> listAll() {
        return new ResponseEntity<>(studantDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id) {
        verifyIfStudantExists(id);
        Studant studant = studantDAO.findOne(id);
        return new ResponseEntity<>(studant, HttpStatus.OK);
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<?> findStudantsByName(@PathVariable String name) {
        return new ResponseEntity<>(studantDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Studant studant) {
        return new ResponseEntity<>(studantDAO.save(studant), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyIfStudantExists(id);
        studantDAO.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Studant studant) {
        verifyIfStudantExists(studant.getId());
        studantDAO.save(studant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyIfStudantExists(Long id) {
        if (studantDAO.findOne(id) == null) {
            throw new ResourceNotFoundException("Studant not found for ID: " + id);
        }
    }

}
