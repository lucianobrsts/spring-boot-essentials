package br.com.devdojo.javaclient;

import br.com.devdojo.model.PageableResponse;
import br.com.devdojo.model.Studant;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class JavaSpringClientTest {
    public static void main(String[] args) {

        Studant studantPost = new Studant();
        studantPost.setName("John Wick");
        studantPost.setEmail("john@pencil.com");
        JavaClientDAO dao = new JavaClientDAO();

        System.out.println(dao.findById(1));
        System.out.println(dao.listAll());
        System.out.println(dao.save(studantPost));
    }

}
