package br.com.devdojo.awesome.endpoint;

import br.com.devdojo.awesome.model.Studant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("studant")
public class StudanteEndPoint {

    @RequestMapping(method = RequestMethod.GET, path = "/list")
    public List<Studant> listAll() {
        return asList(new Studant("Deku"), new Studant("Todoroki"));
    }

}
