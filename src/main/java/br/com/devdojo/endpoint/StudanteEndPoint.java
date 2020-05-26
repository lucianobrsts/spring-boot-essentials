package br.com.devdojo.endpoint;

import br.com.devdojo.error.CustomErrorType;
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
        Studant studant = studantDAO.findOne(id);
        if (studant == null) {
            return new ResponseEntity<>(new CustomErrorType("Estudent not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(studant, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Studant studant) {
        return new ResponseEntity<>(studantDAO.save(studant), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        studantDAO.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Studant studant) {
        studantDAO.save(studant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
