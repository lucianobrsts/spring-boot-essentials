package br.com.devdojo.endpoint;

import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Studant;
import br.com.devdojo.repository.StudantRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1")
public class StudanteEndPoint {
    private final StudantRepository studantDAO;

    //Faz parte do framework de injeção de dependencias do spring, ele faz a injeção das dependências e instancia o objeto para nós.
    @Autowired
    public StudanteEndPoint(StudantRepository studantDAO) {
        this.studantDAO = studantDAO;
    }

    @GetMapping(path = "admin/studants")
    @ApiOperation(value = "Return a list with all studants", response = Studant[].class)

//Configuração local para pegar token na documentação
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
//    })
    public ResponseEntity<?> listAll(Pageable pageable) {
        return new ResponseEntity<>(studantDAO.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "protected/studants/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id, Authentication authentication) {
        System.out.println(authentication);
        verifyIfStudantExists(id);
        Studant studant = studantDAO.findOne(id);
        return new ResponseEntity<>(studant, HttpStatus.OK);
    }

    @GetMapping(path = "protected/studants/findByName/{name}")
    public ResponseEntity<?> findStudantsByName(@PathVariable String name) {
        return new ResponseEntity<>(studantDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    @PostMapping(path = "admin/studants/")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Studant studant) {
        return new ResponseEntity<>(studantDAO.save(studant), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "admin/studants/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyIfStudantExists(id);
        studantDAO.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "admin/studants/")
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
