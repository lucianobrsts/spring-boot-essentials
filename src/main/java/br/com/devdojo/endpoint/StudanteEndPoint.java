package br.com.devdojo.endpoint;

import br.com.devdojo.error.CustomErrorType;
import br.com.devdojo.model.Studant;
import br.com.devdojo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("studants")
public class StudanteEndPoint {
    private final DateUtil dateUtil;

    //Faz parte do framework de injeção de dependencias do spring, ele faz a injeção das dependências e instancia o objeto para nós.
    @Autowired
    public StudanteEndPoint(DateUtil dateUtil) {
        this.dateUtil = dateUtil;
    }

    @GetMapping
    public ResponseEntity<?> listAll() {
//        System.out.println(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(Studant.studantList, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") int id) {
        Studant studant = new Studant();
        studant.setId(id);
        int index = Studant.studantList.indexOf(studant);
        if(index == -1) {
            return new ResponseEntity<>(new CustomErrorType("Estudent not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Studant.studantList.get(index), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Studant studant) {
        Studant.studantList.add(studant);

        return new ResponseEntity<>(studant, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Studant studant) {
        Studant.studantList.remove(studant);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Studant studant) {
        Studant.studantList.remove(studant);
        Studant.studantList.add(studant);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
