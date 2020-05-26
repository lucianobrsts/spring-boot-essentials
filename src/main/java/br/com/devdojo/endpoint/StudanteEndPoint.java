package br.com.devdojo.endpoint;

import br.com.devdojo.model.Studant;
import br.com.devdojo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> listAll() {
//        System.out.println(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(Studant.studantList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") int id) {
        Studant studant = new Studant();
        studant.setId(id);
        int index = Studant.studantList.indexOf(studant);
        if(index == -1)
    }

}
